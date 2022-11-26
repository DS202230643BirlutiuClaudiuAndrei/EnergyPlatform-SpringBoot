package dsrl.energy.service.general;

import dsrl.energy.dto.measurement.ChartDataMapper;
import dsrl.energy.dto.measurement.ChartDataSetDTO;
import dsrl.energy.dto.notification.NotificationDTO;
import dsrl.energy.dto.notification.NotificationMapper;
import dsrl.energy.dto.rabbitmq.ConsumptionQueueDTO;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.entity.Measurement;
import dsrl.energy.model.entity.MeteringDevice;
import dsrl.energy.model.entity.Notification;
import dsrl.energy.repository.MeasurementRepository;
import dsrl.energy.repository.MeteringDeviceRepository;
import dsrl.energy.repository.NotificationRepository;
import dsrl.energy.service.exception.MaxConsumptionExceededException;
import dsrl.energy.service.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MeasurementService {

    public static String NOTIFICATION_FORMAT = "%s exceeded the max consumption with %f. The last measurement: %s ;the current one: %s";
    private final MeasurementRepository measurementRepository;
    private final MeteringDeviceRepository meteringDeviceRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, MeteringDeviceRepository meteringDeviceRepository, NotificationRepository notificationRepository) {
        this.measurementRepository = measurementRepository;
        this.meteringDeviceRepository = meteringDeviceRepository;
        this.notificationRepository = notificationRepository;
    }


    /**
     * This method returns the data set for chart which display consumption on day
     *
     * @param deviceId the device id
     * @param ownerId  thw  owner id
     * @param day      the day
     * @return return a list of dataset
     */
    public List<ChartDataSetDTO> getDataSetChart(UUID deviceId, UUID ownerId, LocalDateTime day) {
        LocalDateTime startDate = day.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endDate = day.truncatedTo(ChronoUnit.DAYS).plusDays(1).minusMinutes(1);
        List<Measurement> measurements = measurementRepository.findMeasurementsByDeviceAndClientPerDay(deviceId, ownerId, startDate, endDate);
        return measurements.stream().map(ChartDataMapper::toDto).collect(Collectors.toList());

    }

    /**
     * This method save for a device a measurement
     *
     * @param consumptionQueueDTO the measurement details
     */
    public void saveFromSensor(ConsumptionQueueDTO consumptionQueueDTO) {

        MeteringDevice meteringDevice = meteringDeviceRepository.findById(consumptionQueueDTO.getDeviceId()).orElseThrow(() -> new ResourceNotFoundException("Device", "id", consumptionQueueDTO.getDeviceId().toString()));

        EnergyUser energyUser = meteringDevice.getOwner();
        Optional<Measurement> lastMeasurementOptional = measurementRepository.findTopByMeteringDeviceAndForEnergyUserOrderByTimeStampDesc(meteringDevice, meteringDevice.getOwner());
        Measurement lastMeasurement=null;
        if(lastMeasurementOptional.isPresent()) {
            lastMeasurement = lastMeasurementOptional.get();
            log.info("Current measurement");
        }
        log.info("Saving the new measurement" + consumptionQueueDTO);
        Measurement newMeasurement = Measurement.builder().consumption(consumptionQueueDTO.getMeasurementValue()).timeStamp(consumptionQueueDTO.getTimeStamp()).meteringDevice(meteringDevice).forEnergyUser(energyUser).build();
        measurementRepository.save(newMeasurement);

        checkExceededConsumption(meteringDevice, lastMeasurement, newMeasurement);
    }

    private void checkExceededConsumption(MeteringDevice meteringDevice, Measurement lastMeasurement, Measurement newMeasurement) {
        if(lastMeasurement == null)
            return;
        double difference = newMeasurement.getConsumption() - lastMeasurement.getConsumption();
        if (difference <= meteringDevice.getMaxHourlyConsumption() || meteringDevice.getOwner() == null) return;

        String description = String.format(NOTIFICATION_FORMAT, meteringDevice.getDescription(), difference, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(lastMeasurement.getTimeStamp()), DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(newMeasurement.getTimeStamp()));

        log.info("create notification" + description);

        Notification notification = Notification.builder().description(description).timeStamp(newMeasurement.getTimeStamp()).owner(meteringDevice.getOwner()).build();
        notificationRepository.save(notification);

        NotificationDTO notificationDTO = NotificationMapper.toDTO(notification);
        throw new MaxConsumptionExceededException("Max value exceeded", notificationDTO.getOwnerId(), notificationDTO);

    }

}

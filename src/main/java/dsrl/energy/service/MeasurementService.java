package dsrl.energy.service;

import dsrl.energy.dto.measurement.ChartDataMapper;
import dsrl.energy.dto.measurement.ChartDataSetDTO;
import dsrl.energy.model.entity.Measurement;
import dsrl.energy.repository.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MeasurementService {

    private final MeasurementRepository measurementRepository;


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
        return measurements.stream().map(ChartDataMapper::toDto).toList();

    }
}

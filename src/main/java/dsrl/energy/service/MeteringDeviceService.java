package dsrl.energy.service;


import dsrl.energy.dto.metteringdevice.MeteringDeviceDTO;
import dsrl.energy.dto.metteringdevice.MeteringDeviceMapper;
import dsrl.energy.dto.metteringdevice.PostMeteringDeviceDTO;
import dsrl.energy.model.entity.MeteringDevice;
import dsrl.energy.repository.MeteringDeviceRepository;
import dsrl.energy.service.exception.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MeteringDeviceService {

    private final MeteringDeviceRepository meteringDeviceRepository;

    public List<MeteringDeviceDTO> getMeteringDevices(UUID userID) {
        List<MeteringDevice> meteringDevices = meteringDeviceRepository.findByOwnerId(userID);
        return meteringDevices.stream().map(MeteringDeviceMapper::toDTO).collect(Collectors.toList());
    }

    public void createDevice(PostMeteringDeviceDTO meteringDeviceDTO) {
        MeteringDevice meteringDevice = MeteringDevice.builder()
                .description(meteringDeviceDTO.getDescription())
                .address(meteringDeviceDTO.getAddress())
                .maxHourlyConsumption(meteringDeviceDTO.getMaxHourlyConsumption())
                .build();
        try {
            meteringDeviceRepository.saveAndFlush(meteringDevice);
        } catch (DataIntegrityViolationException exc) {
            log.error(exc.getMessage());
            throw new ConstraintViolationException("Could not save the metering device");
        }
    }
}

package dsrl.energy.service;


import dsrl.energy.dto.ClientInfoDTO;
import dsrl.energy.dto.mapper.UserMapper;
import dsrl.energy.dto.metteringdevice.DeviceManagementDTO;
import dsrl.energy.dto.metteringdevice.MeteringDeviceDTO;
import dsrl.energy.dto.metteringdevice.MeteringDeviceMapper;
import dsrl.energy.dto.metteringdevice.PostMeteringDeviceDTO;
import dsrl.energy.model.entity.MeteringDevice;
import dsrl.energy.repository.MeteringDeviceRepository;
import dsrl.energy.service.exception.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        MeteringDevice meteringDevice = MeteringDevice.builder().description(meteringDeviceDTO.getDescription()).address(meteringDeviceDTO.getAddress()).maxHourlyConsumption(meteringDeviceDTO.getMaxHourlyConsumption()).build();
        try {
            meteringDeviceRepository.saveAndFlush(meteringDevice);
        } catch (DataIntegrityViolationException exc) {
            log.error(exc.getMessage());
            throw new ConstraintViolationException("Could not save the metering device");
        }
    }


    /**
     * This method returns a json object with the information used for pagination
     *
     * @param pageSize   the max item per page
     * @param pageNumber the current page to be displayed
     * @return a json object with the relevant information
     */
    public Map<String, Object> fetchAllMeteringDevices(int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("maxHourlyConsumption"));
        Page<MeteringDevice> retrievedData = meteringDeviceRepository.findAll(pageable);
        List<DeviceManagementDTO> meteringDevices = retrievedData.getContent().stream().map(meteringDevice -> {
            MeteringDeviceDTO meteringDeviceDTO = MeteringDeviceMapper.toDTO(meteringDevice);
            ClientInfoDTO clientInfoDTO = meteringDevice.getOwner() != null ? UserMapper.clientToDTO(meteringDevice.getOwner()) : null;
            return new DeviceManagementDTO(meteringDeviceDTO, clientInfoDTO);
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("meteringDevices", meteringDevices);
        response.put("currentPage", retrievedData.getNumber());
        response.put("totalItems", retrievedData.getTotalElements());
        response.put("totalPages", retrievedData.getTotalPages());

        return response;
    }

    /**
     * This method deletes a device by id
     *
     * @param deviceId the id of the device
     */
    public void deleteDevice(UUID deviceId) {
        try {
            meteringDeviceRepository.deleteById(deviceId);
        } catch (Exception exc) {
            log.error("An error occurred while deleting the device message=" + exc.getMessage());
            throw new ConstraintViolationException("The device could not be deleted");
        }
    }
}

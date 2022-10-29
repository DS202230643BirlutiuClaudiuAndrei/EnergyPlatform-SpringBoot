package dsrl.energy.service;


import dsrl.energy.dto.energyuser.ClientInfoDTO;
import dsrl.energy.dto.energyuser.UserMapper;
import dsrl.energy.dto.metteringdevice.*;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.entity.MeteringDevice;
import dsrl.energy.repository.EnergyUserRepository;
import dsrl.energy.repository.MeteringDeviceRepository;
import dsrl.energy.service.exception.ConstraintViolationException;
import dsrl.energy.service.exception.ResourceNotFoundException;
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
    private final EnergyUserRepository energyUserRepository;

    public List<MeteringDeviceDTO> getMeteringDevices(UUID userID) {
        List<MeteringDevice> meteringDevices = meteringDeviceRepository.findByOwnerId(userID);
        return meteringDevices.stream().map(MeteringDeviceMapper::toDTO).collect(Collectors.toList());
    }

    public void createDevice(PostMeteringDeviceDTO meteringDeviceDTO) {
        EnergyUser energyUser = meteringDeviceDTO.getOwnerId() != null ? energyUserRepository.findById(meteringDeviceDTO.getOwnerId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", meteringDeviceDTO.getOwnerId().toString())) : null;
        MeteringDevice meteringDevice = MeteringDevice.builder().description(meteringDeviceDTO.getDescription()).address(meteringDeviceDTO.getAddress()).maxHourlyConsumption(meteringDeviceDTO.getMaxHourlyConsumption()).owner(energyUser).build();
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
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("maxHourlyConsumption").and(Sort.by("description")));
        Page<MeteringDevice> retrievedData = meteringDeviceRepository.findAll(pageable);
        List<DeviceManagementDTO> meteringDevices = retrievedData.getContent().stream().map(meteringDevice -> {
            MeteringDeviceDTO meteringDeviceDTO = MeteringDeviceMapper.toDTO(meteringDevice);
            ClientInfoDTO clientInfoDTO = meteringDevice.getOwner() != null ? UserMapper.clientToDTO(meteringDevice.getOwner()) : null;
            return new DeviceManagementDTO(meteringDeviceDTO, clientInfoDTO);
        }).collect(Collectors.toList());;

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

    /**
     * This method is used to update the device information or to set a new owner for device
     *
     * @param deviceDTO the information provided for update
     */
    public void updateDevice(PutMeteringDeviceDTO deviceDTO) {
        MeteringDevice meteringDevice = meteringDeviceRepository.findById(deviceDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Device", "id", deviceDTO.getId().toString()));

        EnergyUser owner = deviceDTO.getOwner() != null ? energyUserRepository.findById(deviceDTO.getOwner()).orElseThrow(() -> new ResourceNotFoundException("User", "id", deviceDTO.getOwner().toString())) : null;
        meteringDevice.setOwner(owner);
        meteringDevice.setDescription(deviceDTO.getDescription());
        meteringDevice.setAddress(meteringDevice.getAddress());
        meteringDevice.setMaxHourlyConsumption(deviceDTO.getMaxHourlyConsumption());
        try {
            meteringDeviceRepository.saveAndFlush(meteringDevice);
        } catch (DataIntegrityViolationException exc) {
            log.error("Occurred while deleting the device; message=" + exc.getMessage());
            throw new ConstraintViolationException("Error occurred while deleting the device");
        }

    }

    /**
     * This method returns with pagination details about owned devices
     *
     * @param pageSize   the max items per page
     * @param pageNumber the current page number
     * @param userId     user id for which are search the owned devices
     * @return a map with the needed fields
     */
    public Map<String, Object> fetAllOwnedDevices(int pageSize, int pageNumber, UUID userId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("maxHourlyConsumption").and(Sort.by("description")));
        Page<MeteringDevice> retrievedData = meteringDeviceRepository.findByOwnerId(userId, pageable);
        List<MeteringDeviceDTO> meteringDeviceList = retrievedData.getContent().stream().map(MeteringDeviceMapper::toDTO).collect(Collectors.toList());

        Map<String, Object> r = new HashMap<>();
        r.put("currentPage",retrievedData.getNumber());
        r.put("totalItems", retrievedData.getTotalElements());
        r.put("totalPages", retrievedData.getTotalPages());
        r.put("meteringDevices", meteringDeviceList);
        return r;
    }
}

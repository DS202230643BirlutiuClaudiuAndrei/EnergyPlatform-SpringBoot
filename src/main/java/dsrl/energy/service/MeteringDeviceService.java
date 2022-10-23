package dsrl.energy.service;


import dsrl.energy.dto.metteringdevice.MeteringDeviceDTO;
import dsrl.energy.dto.metteringdevice.MeteringDeviceMapper;
import dsrl.energy.model.entity.MeteringDevice;
import dsrl.energy.repository.MeteringDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MeteringDeviceService {

    private final MeteringDeviceRepository meteringDeviceRepository;

    public List<MeteringDeviceDTO> getMeteringDevices(UUID userID){
        List<MeteringDevice> meteringDevices = meteringDeviceRepository.findByOwnerId(userID);
        return meteringDevices.stream().map(MeteringDeviceMapper::toDTO).collect(Collectors.toList());
    }
}

package dsrl.energy.dto.metteringdevice;


import dsrl.energy.model.entity.MeteringDevice;

public final class MeteringDeviceMapper {

    public static MeteringDeviceDTO toDTO(MeteringDevice meteringDevice){
        return MeteringDeviceDTO.builder()
                .address(meteringDevice.getAddress())
                .description(meteringDevice.getDescription())
                .id(meteringDevice.getId())
                .maxHourlyConsumption(meteringDevice.getMaxHourlyConsumption())
                .build();
    }

}

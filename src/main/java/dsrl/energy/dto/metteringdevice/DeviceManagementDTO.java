package dsrl.energy.dto.metteringdevice;

import dsrl.energy.dto.energyuser.ClientInfoDTO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DeviceManagementDTO {

    private MeteringDeviceDTO device;
    private ClientInfoDTO clientInfoDTO;
}

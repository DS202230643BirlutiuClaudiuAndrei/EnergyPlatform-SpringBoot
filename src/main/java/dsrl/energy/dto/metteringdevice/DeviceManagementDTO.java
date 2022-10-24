package dsrl.energy.dto.metteringdevice;

import dsrl.energy.dto.ClientInfoDTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DeviceManagementDTO {

    private MeteringDeviceDTO device;
    private ClientInfoDTO clientInfoDTO;
}

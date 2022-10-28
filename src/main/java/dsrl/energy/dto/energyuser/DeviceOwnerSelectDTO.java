package dsrl.energy.dto.energyuser;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceOwnerSelectDTO {

    private UUID ownerId;
    private String email;

}

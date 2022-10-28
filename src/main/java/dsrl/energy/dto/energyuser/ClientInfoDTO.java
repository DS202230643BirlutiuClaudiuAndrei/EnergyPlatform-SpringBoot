package dsrl.energy.dto.energyuser;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ClientInfoDTO {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

}

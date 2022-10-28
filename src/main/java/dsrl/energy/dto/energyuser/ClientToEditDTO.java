package dsrl.energy.dto.energyuser;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientToEditDTO {

    @NotNull
    private UUID id;
    @NotNull
    @Size(min = 1, max = 100)
    private String firstName;
    @NotNull
    @Size(min = 1, max = 100)
    private String lastName;
    @NotNull
    @Size(min = 1, max = 100)
    @Email
    private String email;
}

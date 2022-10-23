package dsrl.energy.dto.authentication;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialsDTO {

    @Email
    @Size(min = 1, max = 100)
    @NotNull
    private String email;
    @Size(min = 1, max = 100)
    @NotNull
    private String password;

}

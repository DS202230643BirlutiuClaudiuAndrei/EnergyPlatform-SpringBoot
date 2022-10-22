package dsrl.energy.dto.authentication;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
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

package dsrl.energy.dto.mapper.authentication;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoRegisterDTO {

    @NotNull
    @Email
    @Size(min=1, max =100)
    private String Email;
    @NotNull
    @Size(min=1, max = 100)
    private String firstName;
    @NotNull
    @Size(min=1, max = 100)
    private String lastName;
    @NotNull
    @Size(min=1, max = 100)
    private String password;

}

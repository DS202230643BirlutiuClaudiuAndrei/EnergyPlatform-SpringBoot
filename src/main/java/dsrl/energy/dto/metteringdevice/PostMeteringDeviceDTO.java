package dsrl.energy.dto.metteringdevice;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostMeteringDeviceDTO {
    @NotNull
    private String description;
    @NotNull
    private String address;
    @NotNull
    private Double maxHourlyConsumption;
    private UUID ownerId;
}

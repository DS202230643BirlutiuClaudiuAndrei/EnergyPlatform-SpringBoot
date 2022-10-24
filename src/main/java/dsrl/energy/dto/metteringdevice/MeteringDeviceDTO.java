package dsrl.energy.dto.metteringdevice;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MeteringDeviceDTO {

    private UUID id;
    private String description;
    private String address;
    private Double maxHourlyConsumption;

}

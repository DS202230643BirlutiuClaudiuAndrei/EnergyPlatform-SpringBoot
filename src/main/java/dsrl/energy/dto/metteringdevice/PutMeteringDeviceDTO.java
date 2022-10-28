package dsrl.energy.dto.metteringdevice;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PutMeteringDeviceDTO {

    private UUID id;
    private String description;
    private String address;
    private Double maxHourlyConsumption;
    private UUID owner;
}

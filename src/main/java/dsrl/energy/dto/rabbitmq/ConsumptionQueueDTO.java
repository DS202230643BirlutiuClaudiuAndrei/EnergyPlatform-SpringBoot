package dsrl.energy.dto.rabbitmq;


import lombok.*;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * This dto is used to extract the response from rabbitmq
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ConsumptionQueueDTO {

    private UUID deviceId;
    private LocalDateTime timeStamp;
    private Double measurementValue;

    public ConsumptionQueueDTO(JSONObject jsonObject){
        this.deviceId = UUID.fromString(jsonObject.getString("deviceId"));
        this.timeStamp = LocalDateTime.parse(jsonObject.getString("timeStamp"));
        this.measurementValue = jsonObject.getDouble("measurementValue");
    }
}

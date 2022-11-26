package dsrl.energy.dto.notification;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {

    private UUID ownerId;
    private LocalDateTime timeStamp;
    private String description;
}

package dsrl.energy.service.exception;


import dsrl.energy.dto.notification.NotificationDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MaxConsumptionExceededException extends RuntimeException {

    private NotificationDTO notificationDTO;
    private UUID userId;


    public MaxConsumptionExceededException(String message,UUID userId, NotificationDTO notificationDTO) {
        super(message);
        this.userId = userId;
        this.notificationDTO = notificationDTO;
    }


}

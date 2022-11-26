package dsrl.energy.dto.notification;


import dsrl.energy.model.entity.Notification;

public final class NotificationMapper {

    public static NotificationDTO toDTO(Notification notification) {
        return NotificationDTO.builder()
                .ownerId(notification.getOwner().getId())
                .timeStamp(notification.getTimeStamp())
                .description(notification.getDescription())
                .build();
    }
}

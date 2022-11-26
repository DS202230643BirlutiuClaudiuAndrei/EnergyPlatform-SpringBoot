package dsrl.energy.service.general;


import dsrl.energy.dto.notification.NotificationDTO;
import dsrl.energy.dto.notification.NotificationMapper;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.entity.Notification;
import dsrl.energy.repository.EnergyUserRepository;
import dsrl.energy.repository.NotificationRepository;
import dsrl.energy.service.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EnergyUserRepository energyUserRepository;


    @Autowired
    public NotificationService(NotificationRepository notificationRepository, EnergyUserRepository energyUserRepository) {
        this.notificationRepository = notificationRepository;
        this.energyUserRepository = energyUserRepository;
    }

    public List<NotificationDTO> getNotifications(String username) {
        EnergyUser energyUser = energyUserRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        Page<Notification> notifications = notificationRepository.findByOwner(energyUser, PageRequest.of(0,20));
        return notifications.getContent().stream().map(NotificationMapper::toDTO).collect(Collectors.toList());
    }
}

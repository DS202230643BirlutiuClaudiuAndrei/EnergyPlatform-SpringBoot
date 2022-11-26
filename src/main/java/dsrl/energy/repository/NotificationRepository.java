package dsrl.energy.repository;


import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByOwner(EnergyUser energyUser, Pageable pageable);
}

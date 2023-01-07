package dsrl.energy.repository;

import dsrl.energy.model.entity.EnergyMessage;
import dsrl.energy.model.entity.EnergyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<EnergyMessage, UUID> {

    List<EnergyMessage> findByRoomOrderByTime(UUID roomId);

    @Query("SELECT m FROM EnergyMessage m  WHERE m.room=:roomId AND m.receiver.id=:receiverId")
    List<EnergyMessage> findByRoomIdAndTransmitterId(@Param("roomId") UUID roomId, @Param("receiverId") UUID receiverId);

    List<EnergyMessage> findByRoom(UUID fromString);
}

package dsrl.energy.repository;

import dsrl.energy.model.entity.MeteringDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MeteringDeviceRepository extends JpaRepository<MeteringDevice, UUID> {
}

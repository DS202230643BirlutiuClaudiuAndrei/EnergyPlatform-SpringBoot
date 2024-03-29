package dsrl.energy.repository;

import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.entity.MeteringDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MeteringDeviceRepository extends JpaRepository<MeteringDevice, UUID> {

    List<MeteringDevice> findByOwnerId(UUID uuid);
    Page<MeteringDevice> findByOwnerId(UUID uuid, Pageable pageable);

}

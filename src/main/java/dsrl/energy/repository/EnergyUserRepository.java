package dsrl.energy.repository;

import dsrl.energy.model.entity.EnergyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnergyUserRepository extends JpaRepository<EnergyUser, UUID> {


    Optional<EnergyUser> findByEmail(String email);
}

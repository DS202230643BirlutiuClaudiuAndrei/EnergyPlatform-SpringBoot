package dsrl.energy.repository;

import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.enums.EnergyUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnergyUserRepository extends JpaRepository<EnergyUser, UUID> {


    Optional<EnergyUser> findByEmail(String email);

    Page<EnergyUser> findByRole(EnergyUserRole role, Pageable pageable);

    List<EnergyUser> findByRole(EnergyUserRole role);
}

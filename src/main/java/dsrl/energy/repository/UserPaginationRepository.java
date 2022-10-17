package dsrl.energy.repository;

import dsrl.energy.model.entity.EnergyUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserPaginationRepository extends PagingAndSortingRepository<EnergyUser, UUID> {

    List<EnergyUser> findAll(Pageable pageable);
}

package dsrl.energy.service;

import dsrl.energy.dto.ClientToCreateDTO;
import dsrl.energy.dto.mapper.UserMapper;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.repository.EnergyUserRepository;
import dsrl.energy.repository.UserPaginationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UserService {
    private final EnergyUserRepository userRepository;
    private final UserPaginationRepository userPaginationRepository;

    @Autowired
    public UserService(EnergyUserRepository userRepository, UserPaginationRepository userPaginationRepository) {
        this.userRepository = userRepository;
        this.userPaginationRepository = userPaginationRepository;
    }

    public void createNewClient(ClientToCreateDTO newClient) {
        EnergyUser toInsertClient = UserMapper.clientToEntity(newClient);
        userRepository.save(toInsertClient);
    }

    /**
     * Implementation based on https://www.bezkoder.com/spring-boot-pagination-filter-jpa-pageable/
     *
     * @param pageSize   items / page
     * @param pageNumber the number of page to retrieve
     * @return a map with the objects necessary for react pagination
     */
    public Map<String, Object> fetchAllClients(Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("email"));
        Page<EnergyUser> retrievedData = userPaginationRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("energyUsers", retrievedData.getContent());
        response.put("currentPage", retrievedData.getNumber());
        response.put("totalItems", retrievedData.getTotalElements());
        response.put("totalPages", retrievedData.getTotalPages());
        return response;
    }

}

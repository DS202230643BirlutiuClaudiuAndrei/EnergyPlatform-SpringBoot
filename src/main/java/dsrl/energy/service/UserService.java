package dsrl.energy.service;

import dsrl.energy.dto.ClientInfoDTO;
import dsrl.energy.dto.ClientToCreateDTO;
import dsrl.energy.dto.ClientToEditDTO;
import dsrl.energy.dto.authentication.InfoRegisterDTO;
import dsrl.energy.dto.authentication.UserAuthMapper;
import dsrl.energy.dto.mapper.UserMapper;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.enums.EnergyUserRole;
import dsrl.energy.repository.EnergyUserRepository;
import dsrl.energy.service.exception.ConstraintViolationException;
import dsrl.energy.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final EnergyUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public Map<String, Object> fetchAllClients(int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("email"));
        Page<EnergyUser> retrievedData = userRepository.findAll(pageable);

        List<ClientInfoDTO> clientInfoDTOList = retrievedData.stream().map(UserMapper::clientToDTO).toList();
        Map<String, Object> response = new HashMap<>();
        response.put("energyUsers", clientInfoDTOList);
        response.put("currentPage", retrievedData.getNumber());
        response.put("totalItems", retrievedData.getTotalElements());
        response.put("totalPages", retrievedData.getTotalPages());

        return response;
    }

    public void updateClient(ClientToEditDTO clientToEditDTO) {
        EnergyUser toUpdateUser = userRepository.findById(clientToEditDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", clientToEditDTO.getId().toString()));
        toUpdateUser.setEmail(clientToEditDTO.getEmail());
        toUpdateUser.setFirstName(clientToEditDTO.getFirstName());
        toUpdateUser.setLastName(clientToEditDTO.getLastName());
    }

    /**
     * This method is called to register a new energy user in application by a user
     *
     * @return a string with the email
     */
    public String registerNewUser(InfoRegisterDTO registerDTO) {
        EnergyUser energyUser = UserAuthMapper.toEntity(registerDTO, EnergyUserRole.CLIENT);
        energyUser.setUserPassword(passwordEncoder.encode(registerDTO.getPassword()));
        EnergyUser savedUser;
        try {
            savedUser = userRepository.saveAndFlush(energyUser);
        } catch (DataIntegrityViolationException e) {
            log.error("Could not save the new client " + e.getMessage());
            throw new ConstraintViolationException("Could not save the new client");
        }
        return energyUser.getEmail();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}

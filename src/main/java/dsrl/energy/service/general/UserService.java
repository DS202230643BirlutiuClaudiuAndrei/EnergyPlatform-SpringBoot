package dsrl.energy.service.general;

import dsrl.energy.dto.authentication.InfoRegisterDTO;
import dsrl.energy.dto.authentication.UserAuthMapper;
import dsrl.energy.dto.energyuser.ClientInfoDTO;
import dsrl.energy.dto.energyuser.ClientToEditDTO;
import dsrl.energy.dto.energyuser.DeviceOwnerSelectDTO;
import dsrl.energy.dto.energyuser.UserMapper;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.entity.MeteringDevice;
import dsrl.energy.model.enums.EnergyUserRole;
import dsrl.energy.repository.EnergyUserRepository;
import dsrl.energy.repository.MeteringDeviceRepository;
import dsrl.energy.service.exception.ConstraintViolationException;
import dsrl.energy.service.exception.DeleteException;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final EnergyUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MeteringDeviceRepository meteringDeviceRepository;

    /**
     * This method is called to register a new energy user in application
     *
     * @return a string with the email
     */
    public String registerNewUser(InfoRegisterDTO infoRegisterDTO, EnergyUserRole energyUserRole) {
        EnergyUser energyUser = UserAuthMapper.toEntity(infoRegisterDTO, energyUserRole);
        energyUser.setUserPassword(passwordEncoder.encode(infoRegisterDTO.getPassword()));
        EnergyUser savedUser;
        try {
            savedUser = userRepository.saveAndFlush(energyUser);
        } catch (DataIntegrityViolationException e) {
            log.error("Could not save the new client " + e.getMessage());
            throw new ConstraintViolationException("Could not save the new user account. The email provided is used by another account");
        }
        return savedUser.getEmail();
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
        Page<EnergyUser> retrievedData = userRepository.findByRole(EnergyUserRole.CLIENT, pageable);

        List<ClientInfoDTO> clientInfoDTOList = retrievedData.stream().map(UserMapper::clientToDTO).collect(Collectors.toList());
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public void deleteUser(UUID userId) {
        //set all devices associated to null
        List<MeteringDevice> meteringDevices = meteringDeviceRepository.findByOwnerId(userId);

        try {
            meteringDevices.forEach(meteringDevice -> {
                meteringDevice.setOwner(null);
                meteringDeviceRepository.saveAndFlush(meteringDevice);
            });
            userRepository.deleteById(userId);
        } catch (Exception exc) {
            log.error("Could not delete user" + exc.getMessage());
            throw new DeleteException("Could not delete the user");
        }

    }

    /**
     * This method is used to fetch all users from db which can be mapped to be owner to a device
     *
     * @return a list of possible device owners
     */
    public List<DeviceOwnerSelectDTO> getPossibleDeviceOwners() {
        List<EnergyUser> owners = userRepository.findByRole(EnergyUserRole.CLIENT);
        return owners.stream().map(UserMapper::ownerToDTO).collect(Collectors.toList());
    }
}

package dsrl.energy.service;

import dsrl.energy.dto.ClientToCreateDTO;
import dsrl.energy.dto.mapper.UserMapper;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.repository.EnergyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    private final EnergyUserRepository userRepository;

    @Autowired
    public UserService(EnergyUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createNewClient(ClientToCreateDTO newClient) {
        EnergyUser toInsertClient = UserMapper.clientToEntity(newClient);
        userRepository.save(toInsertClient);
    }

}

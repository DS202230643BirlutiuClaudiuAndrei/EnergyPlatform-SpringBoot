package dsrl.energy.JsonRpc.chat.common;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import dsrl.energy.JsonRpc.chat.dto.MessageDTO;
import dsrl.energy.JsonRpc.chat.dto.MessageMapper;
import dsrl.energy.JsonRpc.chat.dto.UserDTO;
import dsrl.energy.JsonRpc.chat.dto.UserMapper;
import dsrl.energy.model.entity.EnergyMessage;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.enums.EnergyUserRole;
import dsrl.energy.repository.EnergyUserRepository;
import dsrl.energy.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AutoJsonRpcServiceImpl
public class JsonRpcChatServiceImpl implements JsonRpcChatService {

    private final EnergyUserRepository energyUserRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public JsonRpcChatServiceImpl(EnergyUserRepository energyUserRepository, MessageRepository messageRepository) {
        this.energyUserRepository = energyUserRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public List<UserDTO> getChatUsers() {
        List<EnergyUser> energyUserList = energyUserRepository.findByRole(EnergyUserRole.CLIENT);
        return energyUserList.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> getMessages(UUID roomId) {
        List<EnergyMessage> energyMessages = messageRepository.findByRoomOrderByTime(roomId);
        return energyMessages.stream().map(MessageMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> getCommonMessages() {
        List<EnergyMessage> messages = messageRepository.findByRoom(UUID.fromString("db547e40-8a1a-11ed-a1eb-0242ac120002"));

        return messages.stream().map(MessageMapper::toDTO).collect(Collectors.toList());
    }

}

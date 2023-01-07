package dsrl.energy.JsonRpc.chat.websockets;

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
import dsrl.energy.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author gshine
 * @since 2018/12/11 17:14
 */
@Service
@AutoJsonRpcServiceImpl
public class ChattingServiceImpl implements ChattingService {

    private final MessageRepository messageRepository;
    private final EnergyUserRepository energyUserRepository;

    @Autowired
    public ChattingServiceImpl(MessageRepository messageRepository, EnergyUserRepository energyUserRepository) {
        this.messageRepository = messageRepository;
        this.energyUserRepository = energyUserRepository;
    }

    @Override
    public String hello(String message) {
        return "hello, " + message;
    }

    @Override
    public MessageDTO sendAdminMessage(UUID from, UUID to, String content) {
        EnergyUser toUser = energyUserRepository.findById(to).orElseThrow(() -> new ResourceNotFoundException("client", "id", to.toString()));
        EnergyUser fromUser = energyUserRepository.findById(from).orElseThrow(() -> new ResourceNotFoundException("admin", "id", from.toString()));
        EnergyMessage message = EnergyMessage.builder().time(LocalDateTime.now()).content(content).room(to).receiver(toUser).transmitter(fromUser).isRead(false).build();
        EnergyMessage energyMessage = messageRepository.save(message);
        return MessageMapper.toDTO(energyMessage);
    }

    @Override
    public MessageDTO sendClientMessage(UUID from, String content) {
        EnergyUser fromUser = energyUserRepository.findById(from).orElseThrow(() -> new ResourceNotFoundException("client", "id", from.toString()));
        List<EnergyUser> toUsers = energyUserRepository.findByRole(EnergyUserRole.ADMIN);
        EnergyUser toUser = toUsers.size() > 0 ? toUsers.get(0) : null;
        EnergyMessage message = EnergyMessage.builder().transmitter(fromUser).receiver(toUser).content(content).time(LocalDateTime.now()).isRead(false).room(from).build();
        EnergyMessage savedMessage = messageRepository.save(message);
        return MessageMapper.toDTO(savedMessage);
    }

    @Override
    public String readMessage(UUID messageID) {
        EnergyMessage energyMessage = messageRepository.findById(messageID).orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageID.toString()));
        energyMessage.setRead(true);
        messageRepository.save(energyMessage);
        return "Success!";
    }

    @Override
    public String readAdminAllMessages(UUID roomId, UUID receiver) {
        List<EnergyMessage> messages = messageRepository.findByRoomIdAndTransmitterId(roomId, receiver);
        for (EnergyMessage message : messages) {
            message.setRead(true);
            messageRepository.save(message);
        }
        return "Success";
    }

    @Override
    public UserDTO sendTypingMessage(UUID userId) {
        EnergyUser energyUser = energyUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId.toString()));
        return UserMapper.toDTO(energyUser);
    }

    @Override
    public MessageDTO sendCommonMessage(UUID transmitterId, String content) {
        EnergyUser transmitter = energyUserRepository.findById(transmitterId).orElseThrow(() -> new ResourceNotFoundException("user", "id", transmitterId.toString()));
        EnergyMessage message = EnergyMessage.builder()
                .room(UUID.fromString("db547e40-8a1a-11ed-a1eb-0242ac120002"))
                .isRead(false)
                .transmitter(transmitter)
                .content(content)
                .time(LocalDateTime.now())
                .build();
        EnergyMessage savedMessage = messageRepository.save(message);
        return MessageMapper.toDTO(savedMessage);
    }


}
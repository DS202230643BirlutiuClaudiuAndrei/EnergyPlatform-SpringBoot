package dsrl.energy.JsonRpc.chat.common;

import com.googlecode.jsonrpc4j.JsonRpcService;
import dsrl.energy.JsonRpc.chat.dto.MessageDTO;
import dsrl.energy.JsonRpc.chat.dto.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@JsonRpcService("/chat")
public interface JsonRpcChatService {

    @PreAuthorize("hasAuthority('ADMIN')")
    List<UserDTO> getChatUsers();

    List<MessageDTO> getMessages(UUID roomId);

    List<MessageDTO> getCommonMessages();

}

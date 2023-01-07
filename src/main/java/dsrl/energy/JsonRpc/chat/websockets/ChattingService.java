package dsrl.energy.JsonRpc.chat.websockets;


import com.googlecode.jsonrpc4j.JsonRpcService;
import dsrl.energy.JsonRpc.chat.dto.MessageDTO;
import dsrl.energy.JsonRpc.chat.dto.UserDTO;

import java.util.UUID;

/**
 * @author gshine
 * @since 2018/12/11 17:13
 */
@JsonRpcService("/chatting")
public interface ChattingService {
    String hello(String message);

    MessageDTO sendAdminMessage(UUID from, UUID to, String content);

    MessageDTO sendClientMessage(UUID from, String content);

    String readMessage(UUID messageID);

    String readAdminAllMessages(UUID roomId, UUID receiver);

    UserDTO sendTypingMessage(UUID userId);

    MessageDTO sendCommonMessage(UUID transmitterId, String content);


}
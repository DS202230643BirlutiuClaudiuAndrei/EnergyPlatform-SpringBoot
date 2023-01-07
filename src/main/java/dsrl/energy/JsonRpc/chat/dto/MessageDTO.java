package dsrl.energy.JsonRpc.chat.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MessageDTO implements Serializable {

    private UUID messageId;
    private UserDTO receiver;
    private UserDTO transmitter;
    private UUID roomId;
    private String content;
    private String time;
    private boolean state;

}

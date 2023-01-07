package dsrl.energy.JsonRpc.chat.dto;

import dsrl.energy.model.entity.EnergyMessage;
import dsrl.energy.model.entity.EnergyUser;

public final class MessageMapper {

    public static MessageDTO toDTO(EnergyMessage energyMessage){
        return MessageDTO.builder()
                .messageId(energyMessage.getId())
                .content(energyMessage.getContent())
                .receiver(energyMessage.getReceiver()!=null? UserMapper.toDTO(energyMessage.getReceiver()) : null)
                .transmitter(UserMapper.toDTO(energyMessage.getTransmitter()))
                .roomId(energyMessage.getRoom())
                .time(energyMessage.getTime().toString())
                .state(energyMessage.isRead())
                .build();
    }

}

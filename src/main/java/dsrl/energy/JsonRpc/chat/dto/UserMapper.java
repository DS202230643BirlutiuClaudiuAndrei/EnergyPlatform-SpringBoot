package dsrl.energy.JsonRpc.chat.dto;

import dsrl.energy.model.entity.EnergyUser;

public final class UserMapper {

    public static UserDTO toDTO(EnergyUser energyUser) {
        return new UserDTO(energyUser.getId(), energyUser.getFirstName(), energyUser.getLastName());
    }
}

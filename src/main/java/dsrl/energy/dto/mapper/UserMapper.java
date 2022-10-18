package dsrl.energy.dto.mapper;


import dsrl.energy.dto.ClientInfoDTO;
import dsrl.energy.dto.ClientToCreateDTO;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.enums.EnergyUserRole;

public final class UserMapper {

    public static EnergyUser clientToEntity(ClientToCreateDTO newClient) {
        return EnergyUser.builder()
                .email(newClient.getEmail())
                .firstName(newClient.getFirstName())
                .lastName(newClient.getLastName())
                .role(EnergyUserRole.CLIENT)
                .build();
    }

    public static ClientInfoDTO clientToDTO(EnergyUser client) {
        return ClientInfoDTO.builder()
                .email(client.getEmail())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .id(client.getId())
                .build();
    }


}

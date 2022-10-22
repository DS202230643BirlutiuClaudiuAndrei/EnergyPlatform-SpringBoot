package dsrl.energy.dto.authentication;

import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.enums.EnergyUserRole;


public final class UserAuthMapper {

    public static EnergyUser toEntity(InfoRegisterDTO infoRegisterDTO, EnergyUserRole role) {
        return EnergyUser.builder()
                .email(infoRegisterDTO.getEmail())
                .firstName(infoRegisterDTO.getFirstName())
                .lastName(infoRegisterDTO.getLastName())
                .role(role)
                .build();
    }

}

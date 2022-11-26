package dsrl.energy.controller;

import dsrl.energy.dto.authentication.InfoRegisterDTO;
import dsrl.energy.dto.energyuser.ClientToEditDTO;
import dsrl.energy.dto.energyuser.DeviceOwnerSelectDTO;
import dsrl.energy.dto.httpresponse.DeleteResponseDTO;
import dsrl.energy.dto.httpresponse.PostResponseDTO;
import dsrl.energy.dto.httpresponse.PutResponseDTO;
import dsrl.energy.dto.notification.NotificationDTO;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.enums.EnergyUserRole;
import dsrl.energy.service.general.NotificationService;
import dsrl.energy.service.general.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public UserController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @PostMapping(path = "/admin/client")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PostResponseDTO> createNewClient(@RequestBody @Valid InfoRegisterDTO infoRegisterDTO) {
        String email = userService.registerNewUser(infoRegisterDTO, EnergyUserRole.CLIENT);
        PostResponseDTO postResponseDTO = PostResponseDTO.builder().message("Success creating user with email " + email).build();
        return new ResponseEntity<>(postResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping(path = "/client)")
    public ResponseEntity<String> editClient(@RequestBody @Valid ClientToEditDTO clientToEditDTO) {
        userService.updateClient(clientToEditDTO);
        return new ResponseEntity<>("Succes!", HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/admin/client")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PutResponseDTO> editClientAdmin(@RequestBody @Valid ClientToEditDTO clientToEditDTO) {
        userService.updateClient(clientToEditDTO);
        return new ResponseEntity<>(new PutResponseDTO("Success"), HttpStatus.NO_CONTENT);
    }


    @GetMapping(path = "/client")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllClients(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(name = "pageSize", defaultValue = "3") int pageSize) {
        Map<String, Object> response = userService.fetchAllClients(pageSize, pageNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/client")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DeleteResponseDTO> deleteClient(@RequestParam(name = "id") UUID userId) {
        userService.deleteUser(userId);
        DeleteResponseDTO deleteResponseDTO = new DeleteResponseDTO("User deleted successfully");
        return new ResponseEntity<>(deleteResponseDTO, HttpStatus.OK);
    }

    /**
     * This endpoint is used to fetch all users from db which can be mapped to be owner to a device
     *
     * @return a list of energy user (client)
     */
    @GetMapping(path = "/possible-owners")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<DeviceOwnerSelectDTO>> getPossibleOwners() {
        List<DeviceOwnerSelectDTO> deviceOwnerSelectDTOS = userService.getPossibleDeviceOwners();
        return new ResponseEntity<>(deviceOwnerSelectDTOS, HttpStatus.OK);
    }

    @GetMapping(path = "/notification")
    public ResponseEntity<List<NotificationDTO>> getNotification(Authentication authentication) {
        EnergyUser energyUser = (EnergyUser) authentication.getPrincipal();
        List<NotificationDTO> notificationDTOS = notificationService.getNotifications(energyUser.getUsername());
        return ResponseEntity.ok(notificationDTOS);
    }
}

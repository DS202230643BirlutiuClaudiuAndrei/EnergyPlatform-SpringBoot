package dsrl.energy.controller;

import dsrl.energy.dto.ClientToEditDTO;
import dsrl.energy.dto.httpresponse.DeleteResponseDTO;
import dsrl.energy.dto.httpresponse.PostResponseDTO;
import dsrl.energy.dto.httpresponse.PutResponseDTO;
import dsrl.energy.dto.authentication.InfoRegisterDTO;
import dsrl.energy.model.enums.EnergyUserRole;
import dsrl.energy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/admin/client")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PostResponseDTO> createNewClient(@RequestBody @Valid InfoRegisterDTO infoRegisterDTO) {
        String email = userService.registerNewUser(infoRegisterDTO, EnergyUserRole.CLIENT);
        PostResponseDTO postResponseDTO = PostResponseDTO.builder().message("Success creating user with email " +email).build();
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
    public ResponseEntity<Map<String, Object>> getAllClients(@RequestParam(name="pageNumber",defaultValue = "0") int pageNumber, @RequestParam(name="pageSize",defaultValue = "3") int pageSize) {
        Map<String, Object> response = userService.fetchAllClients(pageSize, pageNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/client")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DeleteResponseDTO> deleteClient(@RequestParam(name="id") UUID userId) {
        userService.deleteUser(userId);
        DeleteResponseDTO deleteResponseDTO = new DeleteResponseDTO("User deleted successfully");
        return new ResponseEntity<>(deleteResponseDTO, HttpStatus.OK);
    }
}

package dsrl.energy.controller;

import dsrl.energy.dto.ClientToEditDTO;
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

@RestController
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/admin/create-client")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> createNewClient(@RequestBody @Valid InfoRegisterDTO infoRegisterDTO) {
        userService.registerNewUser(infoRegisterDTO, EnergyUserRole.CLIENT);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @PutMapping(path = "/client)")
    public ResponseEntity<String> editClient(@RequestBody @Valid ClientToEditDTO clientToEditDTO) {
        userService.updateClient(clientToEditDTO);
        return new ResponseEntity<>("Succes!", HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/client")
    public ResponseEntity<Map<String, Object>> getAllClients(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "3") int pageSize) {

        Map<String, Object> response = userService.fetchAllClients(pageSize, pageNumber);
        return new ResponseEntity<>(response, HttpStatus.FOUND);

    }


}

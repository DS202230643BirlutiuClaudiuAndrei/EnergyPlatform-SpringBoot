package dsrl.energy.controller;

import dsrl.energy.dto.ClientToCreateDTO;
import dsrl.energy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/client")
    public ResponseEntity<String> createNewClient(@RequestBody @Valid ClientToCreateDTO clientToCreateDTO) {
        userService.createNewClient(clientToCreateDTO);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @GetMapping(path = "/client")
    public ResponseEntity<Map<String, Object>> getAllClients(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "3") int pageSize) {

        Map<String, Object> response = userService.fetchAllClients(pageSize, pageNumber);
        return new ResponseEntity<>(response, HttpStatus.FOUND);

    }
}

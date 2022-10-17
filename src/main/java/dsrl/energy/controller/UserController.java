package dsrl.energy.controller;

import dsrl.energy.dto.ClientToCreateDTO;
import dsrl.energy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createNewClient(@RequestBody @Valid ClientToCreateDTO clientToCreateDTO) {
        userService.createNewClient(clientToCreateDTO);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

}

package dsrl.energy.controller;

import dsrl.energy.config.security.TokenProvider;
import dsrl.energy.dto.authentication.CredentialsDTO;
import dsrl.energy.dto.authentication.InfoRegisterDTO;
import dsrl.energy.dto.authentication.ResponseDTO;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.enums.EnergyUserRole;
import dsrl.energy.service.general.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class EnergyGeneralController {

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid CredentialsDTO credentials) throws Exception {

        doAuthentication(credentials.getEmail(), credentials.getPassword());

        String token = tokenProvider.provideToken((EnergyUser) userService.loadUserByUsername(credentials.getEmail()));
        ResponseDTO responseDTO = ResponseDTO.builder().token(token).build();
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody @Valid InfoRegisterDTO infoRegisterDTO) throws Exception {

        //create an account
        String email = userService.registerNewUser(infoRegisterDTO, EnergyUserRole.CLIENT);

        doAuthentication(email, infoRegisterDTO.getPassword());
        String token = tokenProvider.provideToken((EnergyUser) userService.loadUserByUsername(email));
        ResponseDTO responseDTO = ResponseDTO.builder().token(token).build();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    private void doAuthentication(String email, String password) throws Exception {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

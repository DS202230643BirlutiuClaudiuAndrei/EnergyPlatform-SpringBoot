package dsrl.energy.controller;

import dsrl.energy.config.security.TokenProvider;
import dsrl.energy.dto.authentication.CredentialsDTO;
import dsrl.energy.dto.authentication.InfoRegisterDTO;
import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.service.UserService;
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
    public ResponseEntity<String> login(@RequestBody @Valid CredentialsDTO credentials) throws Exception {

        doAuthentication(credentials.getEmail(), credentials.getPassword());

        String token = tokenProvider.provideToken((EnergyUser) userService.loadUserByUsername(credentials.getEmail()));
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid InfoRegisterDTO infoRegisterDTO) throws Exception {

        //create an account
        String email = userService.registerNewUser(infoRegisterDTO);

        doAuthentication(email, infoRegisterDTO.getPassword());
        String token = tokenProvider.provideToken((EnergyUser) userService.loadUserByUsername(email));

        return new ResponseEntity<>(token, HttpStatus.OK);
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

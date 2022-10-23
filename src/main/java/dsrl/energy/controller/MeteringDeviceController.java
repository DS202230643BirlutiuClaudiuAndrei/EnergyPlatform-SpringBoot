package dsrl.energy.controller;

import dsrl.energy.dto.metteringdevice.MeteringDeviceDTO;
import dsrl.energy.service.MeteringDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class MeteringDeviceController {

    private final MeteringDeviceService meteringDeviceService;

    @GetMapping("/metering-devices")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<MeteringDeviceDTO>> getMeteringDevices (@RequestParam("id") @Valid UUID userUId) {
        List<MeteringDeviceDTO> meteringDevices = meteringDeviceService.getMeteringDevices(userUId);
        return new ResponseEntity<>(meteringDevices, HttpStatus.OK);
    }
}

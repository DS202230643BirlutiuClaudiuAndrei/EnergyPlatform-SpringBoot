package dsrl.energy.controller;

import dsrl.energy.dto.httpresponse.PostResponseDTO;
import dsrl.energy.dto.metteringdevice.MeteringDeviceDTO;
import dsrl.energy.dto.metteringdevice.PostMeteringDeviceDTO;
import dsrl.energy.service.MeteringDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class MeteringDeviceController {

    private final MeteringDeviceService meteringDeviceService;

    @GetMapping("/device")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<MeteringDeviceDTO>> getMeteringDevices(@RequestParam("id") @Valid UUID userUId) {
        List<MeteringDeviceDTO> meteringDevices = meteringDeviceService.getMeteringDevices(userUId);
        return new ResponseEntity<>(meteringDevices, HttpStatus.OK);
    }

    @PostMapping("/device")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PostResponseDTO> createNewMeteringDevice(@RequestBody @Valid PostMeteringDeviceDTO postMeteringDeviceDTO) {
        meteringDeviceService.createDevice(postMeteringDeviceDTO);
        PostResponseDTO postResponseDTO = new PostResponseDTO("Success in creating the device");
        return new ResponseEntity<>(postResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping(path = "/device/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllMeteringDevices(@RequestParam(name="pageNumber",defaultValue = "0") int pageNumber, @RequestParam(name="pageSize",defaultValue = "3") int pageSize) {
        Map<String, Object> response = meteringDeviceService.fetchAllMeteringDevices(pageSize, pageNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

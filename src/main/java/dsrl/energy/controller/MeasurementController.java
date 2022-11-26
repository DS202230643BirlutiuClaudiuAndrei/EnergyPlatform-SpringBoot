package dsrl.energy.controller;

import dsrl.energy.dto.measurement.ChartDataSetDTO;
import dsrl.energy.service.general.MeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    @GetMapping("/consumption")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<ChartDataSetDTO>> getDataSetChart(@RequestParam("deviceId") UUID deviceId,
                                                                 @RequestParam("ownerId") UUID ownerId,
                                                                 @RequestParam("day") @NotNull Integer day,
                                                                 @RequestParam("year") @NotNull Integer year,
                                                                  @RequestParam("month") @NotNull Integer month
                                                                 ) {
        LocalDateTime localDateTime = LocalDateTime.of(year,month,day,0,0);
        List<ChartDataSetDTO> dataSetDTOS = measurementService.getDataSetChart(deviceId, ownerId, localDateTime);
        return new ResponseEntity<>(dataSetDTOS, HttpStatus.OK);
    }
}

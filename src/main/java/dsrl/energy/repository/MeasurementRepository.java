package dsrl.energy.repository;

import dsrl.energy.model.entity.EnergyUser;
import dsrl.energy.model.entity.Measurement;
import dsrl.energy.model.entity.MeteringDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {

    @Query("SELECT m FROM Measurement m " +
            "WHERE m.meteringDevice.id=:device and " +
            "m.forEnergyUser.id=:owner and " +
            "m.timeStamp BETWEEN :startDate and :endDate ORDER BY m.timeStamp")
    List<Measurement> findMeasurementsByDeviceAndClientPerDay(@Param("device") UUID meteringDevice,
                                                              @Param("owner") UUID owner,
                                                              @Param("startDate") LocalDateTime startDate,
                                                              @Param("endDate") LocalDateTime endDate);


    Optional<Measurement> findTopByMeteringDeviceAndForEnergyUserOrderByTimeStampDesc(MeteringDevice meteringDevice, EnergyUser owner);
}

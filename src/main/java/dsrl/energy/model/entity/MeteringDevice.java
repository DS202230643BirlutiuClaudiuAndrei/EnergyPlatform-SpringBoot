package dsrl.energy.model.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "energy_metering_devices")
public class MeteringDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "description", nullable = false, length = 255)
    @Size(min = 1, max = 255)
    private String description;

    @Column(name = "address", nullable = false, length = 100)
    @Size(min = 1, max = 100)
    private String address;

    @Column(name = "max_hourly_consumption")
    private Double maxHourlyConsumption;

    @OneToMany(mappedBy = "meteringDevice")
    private Collection<Measurement> measurements;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private EnergyUser owner;

}

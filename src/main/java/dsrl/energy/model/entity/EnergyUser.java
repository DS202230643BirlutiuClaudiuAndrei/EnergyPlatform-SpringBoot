package dsrl.energy.model.entity;


import dsrl.energy.model.enums.EnergyUserRole;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "energy_users")
public class EnergyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    @NotNull
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotNull
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotNull
    private String email;

    @Column(name = "role", nullable = false)
    @NotNull
    private EnergyUserRole role;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Collection<MeteringDevice> meteringDevices;
}

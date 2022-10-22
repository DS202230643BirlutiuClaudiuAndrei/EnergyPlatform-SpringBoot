package dsrl.energy.model.entity;


import dsrl.energy.model.enums.EnergyUserRole;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "energy_users")
public class EnergyUser implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotNull
    @Size(min = 1, max = 100)
    private String email;

    @Column(name = "role", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private EnergyUserRole role;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Collection<MeteringDevice> meteringDevices;

    @Column(name = "user_password",nullable = false)
    @NotNull
    private String userPassword;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities=new LinkedList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(this.getRole().name()));
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

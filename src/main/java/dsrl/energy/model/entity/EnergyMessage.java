package dsrl.energy.model.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "energy_messages")
public class EnergyMessage {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    private String content;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private EnergyUser receiver;
    @ManyToOne
    @JoinColumn(name = "transmitter_id")
    private EnergyUser transmitter;
    private boolean isRead;
    private LocalDateTime time;

    private UUID room;
}

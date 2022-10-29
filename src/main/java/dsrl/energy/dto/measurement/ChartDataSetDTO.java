package dsrl.energy.dto.measurement;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ChartDataSetDTO {

    public String hour;
    public Double value;

}

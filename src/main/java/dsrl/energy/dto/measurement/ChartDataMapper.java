package dsrl.energy.dto.measurement;

import dsrl.energy.model.entity.Measurement;

public final class ChartDataMapper {

    public static ChartDataSetDTO toDto(Measurement measurement){
        return ChartDataSetDTO.builder()
                .hour(String.format("%02d:%02d",measurement.getTimeStamp().getHour(),measurement.getTimeStamp().getMinute() ))
                .value(measurement.getConsumption())
                .build();
    }
}

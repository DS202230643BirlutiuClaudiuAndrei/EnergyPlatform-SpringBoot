package dsrl.energy.service.rabbitmq;

import dsrl.energy.dto.rabbitmq.ConsumptionQueueDTO;
import dsrl.energy.service.exception.MaxConsumptionExceededException;
import dsrl.energy.service.exception.ResourceNotFoundException;
import dsrl.energy.service.general.MeasurementService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Consumer {


    private final SimpMessagingTemplate template;
    private final MeasurementService measurementService;

    @Autowired
    public Consumer(SimpMessagingTemplate template, MeasurementService measurementService) {
        this.template = template;
        this.measurementService = measurementService;
    }

    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String payload) {
        JSONObject jsonObject = new JSONObject(payload);
        ConsumptionQueueDTO consumptionQueueDTO = new ConsumptionQueueDTO(jsonObject);
        try {
            measurementService.saveFromSensor(consumptionQueueDTO);
        } catch (ResourceNotFoundException resourceNotFoundException) {
            log.error(resourceNotFoundException.getMessage());
        } catch (MaxConsumptionExceededException maxConsumptionExceededException) {
            log.warn(maxConsumptionExceededException.getMessage());
            template.convertAndSend("/queue/alert." + maxConsumptionExceededException.getUserId(), maxConsumptionExceededException.getNotificationDTO());
        } catch (Exception exception) {
            log.error("An exception is reached", exception);
        }
    }
}

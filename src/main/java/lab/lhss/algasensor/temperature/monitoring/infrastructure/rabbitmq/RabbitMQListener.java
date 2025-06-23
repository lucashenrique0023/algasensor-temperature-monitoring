package lab.lhss.algasensor.temperature.monitoring.infrastructure.rabbitmq;

import lab.lhss.algasensor.temperature.monitoring.api.model.TemperatureLogData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static lab.lhss.algasensor.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.TEMPERATURE_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    @RabbitListener(queues = TEMPERATURE_QUEUE)
    @SneakyThrows
    public void handle(@Payload TemperatureLogData temperatureLogData, @Headers Map<String, Object> headers) {
        var sensorId = temperatureLogData.getSensorId();
        var temperature = temperatureLogData.getValue();
        log.info("Temperature: {} , SensorID: {}", temperature, sensorId);
        log.info("Headers: {}", headers);

        Thread.sleep(Duration.ofSeconds(5).toMillis());
    }

}

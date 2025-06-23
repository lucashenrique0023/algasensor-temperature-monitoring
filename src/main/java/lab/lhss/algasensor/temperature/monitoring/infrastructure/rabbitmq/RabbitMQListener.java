package lab.lhss.algasensor.temperature.monitoring.infrastructure.rabbitmq;

import lab.lhss.algasensor.temperature.monitoring.api.model.TemperatureLogData;
import lab.lhss.algasensor.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static lab.lhss.algasensor.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.TEMPERATURE_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @RabbitListener(queues = TEMPERATURE_QUEUE)
    @SneakyThrows
    public void handle(@Payload TemperatureLogData temperatureLogData) {
        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5).toMillis());
    }

}

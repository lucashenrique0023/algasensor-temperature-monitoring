package lab.lhss.algasensor.temperature.monitoring.infrastructure.rabbitmq;

import lab.lhss.algasensor.temperature.monitoring.api.model.TemperatureLogData;
import lab.lhss.algasensor.temperature.monitoring.domain.service.SensorAlertService;
import lab.lhss.algasensor.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static lab.lhss.algasensor.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;
    private final SensorAlertService sensorAlertService;

    @RabbitListener(queues = TEMPERATURE_PROCESS_QUEUE, concurrency = "2-3")
    @SneakyThrows
    public void handleTemperatureProcess(@Payload TemperatureLogData temperatureLogData) {
        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
    }

    @RabbitListener(queues = TEMPERATURE_ALERT_QUEUE, concurrency = "2-3")
    @SneakyThrows
    public void handleTemperatureAlert(@Payload TemperatureLogData temperatureLogData) {
        sensorAlertService.handleAlert(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5).toMillis());
    }

}

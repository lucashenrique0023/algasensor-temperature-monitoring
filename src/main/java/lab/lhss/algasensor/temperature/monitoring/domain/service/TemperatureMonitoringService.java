package lab.lhss.algasensor.temperature.monitoring.domain.service;

import jakarta.transaction.Transactional;
import lab.lhss.algasensor.temperature.monitoring.api.model.TemperatureLogData;
import lab.lhss.algasensor.temperature.monitoring.domain.model.SensorId;
import lab.lhss.algasensor.temperature.monitoring.domain.model.SensorMonitoring;
import lab.lhss.algasensor.temperature.monitoring.domain.model.TemperatureLog;
import lab.lhss.algasensor.temperature.monitoring.domain.model.TemperatureLogId;
import lab.lhss.algasensor.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import lab.lhss.algasensor.temperature.monitoring.domain.repository.TemperatureLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureMonitoringService {

    private final SensorMonitoringRepository sensorMonitoringRepository;
    private final TemperatureLogRepository temperatureLogRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogData temperatureLogData) {

        sensorMonitoringRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(sensor -> handleSensorMonitoring(temperatureLogData, sensor),
                        () ->logIgnoredTemperature(temperatureLogData));

    }

    private void handleSensorMonitoring(TemperatureLogData temperatureLogData, SensorMonitoring sensor) {
        if (sensor.isEnabled()) {
            sensor.setLastTemperature(temperatureLogData.getValue());
            sensor.setUpdatedAt(OffsetDateTime.now());
            sensorMonitoringRepository.save(sensor);

            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(temperatureLogData.getId()))
                    .registeredAt(temperatureLogData.getRegisteredAt())
                    .value(temperatureLogData.getValue())
                    .sensorId(new SensorId(temperatureLogData.getSensorId()))
                    .build();
            temperatureLogRepository.save(temperatureLog);

            log.info("Updated temperature: SensorId {} Temperature {}",
                    temperatureLogData.getSensorId(), temperatureLogData.getValue());
        } else {
            logIgnoredTemperature(temperatureLogData);
        }
    }

    private void logIgnoredTemperature(TemperatureLogData temperatureLogData) {
        log.info("Ignored temperature: SensorId {} Temperature {}",
                temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }

}

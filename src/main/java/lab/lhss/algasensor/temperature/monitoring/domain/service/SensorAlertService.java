package lab.lhss.algasensor.temperature.monitoring.domain.service;

import jakarta.transaction.Transactional;
import lab.lhss.algasensor.temperature.monitoring.api.model.TemperatureLogData;
import lab.lhss.algasensor.temperature.monitoring.domain.model.SensorId;
import lab.lhss.algasensor.temperature.monitoring.domain.repository.SensorAlertRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SensorAlertService {

    private final SensorAlertRepository sensorAlertRepository;

    @Transactional
    public void handleAlert(TemperatureLogData temperatureLogData) {
        sensorAlertRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(alert -> {
                    if (alert.getMaxTemperature() != null &&
                            temperatureLogData.getValue().compareTo(alert.getMaxTemperature()) >= 0) {
                        log.info("Alert Max Temp, SensorID: {} , Temp: {}",
                                temperatureLogData.getSensorId(), temperatureLogData.getValue());
                    } else if (alert.getMinTemperature() != null &&
                            temperatureLogData.getValue().compareTo(alert.getMinTemperature()) <= 0) {
                        log.info("Alert Min Temp, SensorID: {} , Temp: {}",
                                temperatureLogData.getSensorId(), temperatureLogData.getValue());
                    } else {
                        ignoredAlert(temperatureLogData);
                    }
                }, () -> ignoredAlert(temperatureLogData));
    }

    private void ignoredAlert(TemperatureLogData temperatureLogData) {
        log.info("Alert Ignored, SensorID: {} , Temp: {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }

}

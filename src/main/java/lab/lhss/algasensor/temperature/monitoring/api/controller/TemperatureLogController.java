package lab.lhss.algasensor.temperature.monitoring.api.controller;

import io.hypersistence.tsid.TSID;
import lab.lhss.algasensor.temperature.monitoring.api.model.TemperatureLogData;
import lab.lhss.algasensor.temperature.monitoring.domain.model.SensorId;
import lab.lhss.algasensor.temperature.monitoring.domain.model.TemperatureLog;
import lab.lhss.algasensor.temperature.monitoring.domain.repository.TemperatureLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures")
@RequiredArgsConstructor
public class TemperatureLogController {

    private final TemperatureLogRepository temperatureLogRepository;

    @GetMapping
    public Page<TemperatureLogData> search(@PathVariable TSID sensorId,
                                           @PageableDefault Pageable pageable) {
        Page<TemperatureLog> temperatureLogs = temperatureLogRepository.findAllBySensorId(
                new SensorId(sensorId), pageable);

        return temperatureLogs.map(temperatureLog ->
                TemperatureLogData.builder()
                        .id(temperatureLog.getId().getValue())
                        .value(temperatureLog.getValue())
                        .registeredAt(temperatureLog.getRegisteredAt())
                        .sensorId(temperatureLog.getSensorId().getValue())
                        .build());
    }

}

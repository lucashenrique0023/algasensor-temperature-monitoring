package lab.lhss.algasensor.temperature.monitoring.domain.repository;

import lab.lhss.algasensor.temperature.monitoring.domain.model.SensorId;
import lab.lhss.algasensor.temperature.monitoring.domain.model.SensorMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMonitoringRepository extends JpaRepository<SensorMonitoring, SensorId> {
}

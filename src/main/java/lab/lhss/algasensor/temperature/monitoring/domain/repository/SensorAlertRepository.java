package lab.lhss.algasensor.temperature.monitoring.domain.repository;

import lab.lhss.algasensor.temperature.monitoring.domain.model.SensorAlert;
import lab.lhss.algasensor.temperature.monitoring.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorAlertRepository extends JpaRepository<SensorAlert, SensorId> {
}

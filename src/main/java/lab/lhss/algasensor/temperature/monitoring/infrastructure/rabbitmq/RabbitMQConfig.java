package lab.lhss.algasensor.temperature.monitoring.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String TEMPERATURE_PROCESS = "temperature-monitoring.process-temperature.v1";
    public static final String TEMPERATURE_PROCESS_Q = TEMPERATURE_PROCESS + ".q";
    public static final String TEMPERATURE_PROCESS_DLQ = TEMPERATURE_PROCESS + ".dlq";

    public static final String TEMPERATURE_ALERT_Q = "temperature-monitoring.alert-temperature.v1.q";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue temperatureProcessQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "");
        arguments.put("x-dead-letter-routing-key", TEMPERATURE_PROCESS_DLQ);
        return QueueBuilder.durable(TEMPERATURE_PROCESS_Q).withArguments(arguments).build();
    }

    @Bean
    public Queue temperatureProcessDeadLetterQueue() {
        return QueueBuilder.durable(TEMPERATURE_PROCESS_DLQ).build();
    }

    @Bean
    public Queue temperatureAlertQueue() {
        return QueueBuilder.durable(TEMPERATURE_ALERT_Q).build();
    }

    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange("temperature-processing.temperature-received.v1.e")
                .durable(true).build();
    }

    @Bean
    public Binding temperatureProcessBinding() {
        return BindingBuilder.bind(temperatureProcessQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding temperatureAlertBinding() {
        return BindingBuilder.bind(temperatureAlertQueue()).to(fanoutExchange());
    }

}

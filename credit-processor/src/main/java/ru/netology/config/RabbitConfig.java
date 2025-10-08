package ru.netology.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    // Очередь, из которой слушаем заявки
    public static final String APPLICATIONS_QUEUE = "credit-applications";

    // Очередь, куда отправляем решения
    public static final String QUEUE_NAME = "credit-decisions";

    @Bean
    public Queue creditApplicationsQueue() {
        return new Queue(APPLICATIONS_QUEUE, true);
    }

    @Bean
    public Queue creditDecisionQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("rabbitmq");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}

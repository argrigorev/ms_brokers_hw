package ru.netology.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.netology.event.CreditApplicationEvent;

@Service
public class CreditProcessorService {
    private final CreditDecisionService creditDecisionService;

    public CreditProcessorService(CreditDecisionService creditDecisionService) {
        this.creditDecisionService = creditDecisionService;
    }

    // Подписываемся на очередь заявок
    @KafkaListener(topics = "credit-applications", groupId = "credit-processor-group")
    public void listen(CreditApplicationEvent event) {
        System.out.println("Получена заявка на кредит: " + event.getId());
        creditDecisionService.processCreditApplication(event);
    }
}

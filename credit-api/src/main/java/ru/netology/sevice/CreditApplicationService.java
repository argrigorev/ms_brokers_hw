package ru.netology.sevice;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.dto.CreditApplicationRequest;
import ru.netology.entity.CreditApplication;
import ru.netology.event.CreditApplicationEvent;
import ru.netology.model.ApplicationStatus;
import ru.netology.repository.CreditApplicationRepository;

@Service
public class CreditApplicationService {
    private final CreditApplicationRepository repository;
    private final KafkaTemplate<String, CreditApplicationEvent> kafkaTemplate;

    public CreditApplicationService(CreditApplicationRepository repository,
                                    KafkaTemplate<String, CreditApplicationEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Long createApplication(CreditApplicationRequest request) {
        CreditApplication application = new CreditApplication();
        BeanUtils.copyProperties(request, application);
        application.setStatus(ApplicationStatus.PROCESSING);
        application = repository.save(application);

        CreditApplicationEvent event = new CreditApplicationEvent(
                application.getId(),
                application.getAmount(),
                application.getTerm(),
                application.getIncome(),
                application.getCurrentCreditLoad(),
                application.getCreditRating(),
                application.getStatus()
        );

        kafkaTemplate.send("credit-applications", event);
        System.out.println("Заявка отправлена в Kafka: " + event.getId());
        return application.getId();
    }

    public ApplicationStatus getApplicationStatus(Long id) {
        return repository.findById(id)
                .map(CreditApplication::getStatus)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void updateStatus(Long id, ApplicationStatus newStatus) {
        CreditApplication application = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        application.setStatus(newStatus);
        repository.save(application);
    }
}

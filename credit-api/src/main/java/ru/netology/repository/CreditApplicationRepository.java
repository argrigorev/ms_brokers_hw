package ru.netology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.entity.CreditApplication;

@Repository
public interface CreditApplicationRepository extends JpaRepository<CreditApplication, Long> {

}

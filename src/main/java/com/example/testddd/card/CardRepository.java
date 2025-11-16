package com.example.testddd.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByLink(String link);

    List<Card> findAllBySectionKeyOrderByTitleAsc(String sectionKey);
}

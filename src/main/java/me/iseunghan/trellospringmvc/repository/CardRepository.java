package me.iseunghan.trellospringmvc.repository;

import me.iseunghan.trellospringmvc.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findCardsByPocket_Id(Long pocketId);
}

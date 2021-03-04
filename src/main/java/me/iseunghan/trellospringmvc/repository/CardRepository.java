package me.iseunghan.trellospringmvc.repository;

import me.iseunghan.trellospringmvc.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

}

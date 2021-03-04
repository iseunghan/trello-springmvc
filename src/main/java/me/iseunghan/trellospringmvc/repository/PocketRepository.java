package me.iseunghan.trellospringmvc.repository;

import me.iseunghan.trellospringmvc.domain.Pocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PocketRepository extends JpaRepository<Pocket, Long> {

}

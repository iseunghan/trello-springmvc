package me.iseunghan.trellospringmvc.repository;

import me.iseunghan.trellospringmvc.domain.Pocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PocketRepository extends JpaRepository<Pocket, Long> {
    List<Pocket> findPocketsByBoard_Id(Long boardId);
    List<Pocket> findPocketsByBoard_idOrderByPosition(Long boardId);
}

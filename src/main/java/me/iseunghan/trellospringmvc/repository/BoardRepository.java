package me.iseunghan.trellospringmvc.repository;

import me.iseunghan.trellospringmvc.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}

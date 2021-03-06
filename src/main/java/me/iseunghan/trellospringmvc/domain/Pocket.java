package me.iseunghan.trellospringmvc.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pocket {

    @Id @GeneratedValue
    private Long id;

    private int position;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")  // 외래 키 매핑 전략으로 찾기 때문에 생략 가능.
    private Board board;

    @OneToMany(mappedBy = "pocket", fetch = FetchType.EAGER)
    private List<Card> cards = new ArrayList<>();

    /**
     * 연관관계 편의메소드 정의(양방향 저장)
     * @param board
     */
    public void updateBoard(Board board) {
        this.board = board;
        board.getPockets().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}

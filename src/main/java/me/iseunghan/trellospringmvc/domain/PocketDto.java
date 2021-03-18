package me.iseunghan.trellospringmvc.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PocketDto {
    private Long pocketId;
    private String title;
    private int position;
    private Long boardId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CardDto> cards = new ArrayList<>();

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Long getBoardId() {
        return this.boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public List<CardDto> getCards() {
        return cards;
    }

    public void setCards(List<CardDto> cards) {
        this.cards = cards;
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

    public Long getPocketId() {
        return pocketId;
    }

    public void setPocketId(Long pocketId) {
        this.pocketId = pocketId;
    }
}
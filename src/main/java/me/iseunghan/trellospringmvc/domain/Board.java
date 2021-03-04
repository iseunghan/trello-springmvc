package me.iseunghan.trellospringmvc.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Board {

    @Id
    @GeneratedValue
    private Long id;

    @GeneratedValue
    private Long position;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BoardColor boardColor;

    @OneToMany(mappedBy = "board")
    private List<Pocket> pockets;

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

    public List<Pocket> getPockets() {
        return pockets;
    }

    public void setPockets(List<Pocket> pockets) {
        this.pockets = pockets;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public BoardColor getBoardColor() {
        return boardColor;
    }

    public void setBoardColor(BoardColor boardColor) {
        this.boardColor = boardColor;
    }
}

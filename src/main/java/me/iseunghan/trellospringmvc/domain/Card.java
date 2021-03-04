package me.iseunghan.trellospringmvc.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {

    @Id @GeneratedValue
    private Long id;

    private int position;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;

    @ManyToOne
    @JoinColumn(name = "POCKET_ID")
    private Pocket pocket;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Pocket getPocket() {
        return pocket;
    }

    public void setPocket(Pocket pocket) {
        this.pocket = pocket;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

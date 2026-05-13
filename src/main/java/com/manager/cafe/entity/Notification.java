package com.manager.cafe.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String message;

    private String type;

    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    private Integer tableNo;

    private String orderId;

    private String redirectTab;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTableNo() {
        return tableNo;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }

    public String getRedirectTab() {
        return redirectTab;
    }

    public void setRedirectTab(String redirectTab) {
        this.redirectTab = redirectTab;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Notification(Long id, String redirectTab, String orderId, Integer tableNo, User user, LocalDateTime createdAt, String type, String title, String message) {
        this.id = id;
        this.redirectTab = redirectTab;
        this.orderId = orderId;
        this.tableNo = tableNo;
        this.user = user;
        this.createdAt = createdAt;
        this.type = type;
        this.title = title;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + user +
                ", tableNo=" + tableNo +
                ", orderId='" + orderId + '\'' +
                ", redirectTab='" + redirectTab + '\'' +
                '}';
    }

    public Notification() {
    }
}

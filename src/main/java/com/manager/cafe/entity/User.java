package com.manager.cafe.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    public enum Role {
        ADMIN,
        MANAGER,
        CASHIER,
        WAITER,
        KITCHEN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    private String phone;

    @Column(unique = true)
    private String email;

    private String password;

    private Integer tableCount;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTableCount() {
        return tableCount;
    }

    public void setTableCount(Integer tableCount) {
        this.tableCount = tableCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", tableCount=" + tableCount +
                ", role=" + role +
                ", owner=" + owner +
                '}';
    }

    public User(Long id, User owner, Role role, String password, Integer tableCount, String phone, String email, String name) {
        this.id = id;
        this.owner = owner;
        this.role = role;
        this.password = password;
        this.tableCount = tableCount;
        this.phone = phone;
        this.email = email;
        this.name = name;
    }

    public User() {
    }

}

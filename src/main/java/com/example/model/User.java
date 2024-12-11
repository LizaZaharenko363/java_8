package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.findByEmail",
                query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.findActiveUsers",
                query = "SELECT u FROM User u WHERE u.active = true")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @Column(nullable = false)
    private Boolean active;

    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.registrationDate = LocalDate.now();
        this.active = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}

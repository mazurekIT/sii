package pl.mazurekIT.sii.model;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @Email
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

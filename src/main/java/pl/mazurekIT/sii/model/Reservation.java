package pl.mazurekIT.sii.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    private Long userId;

    public Reservation(String code, Long userId) {
        this.code = code;
        this.userId = userId;
    }

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

}
package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


/**
 * Класс Email представляет собой сущность, которая моделирует электронное письмо.
 * */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {
    @Serial
    private static final long serialVersionUID = 7986377905998595295L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference(value = "user-email")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    private String content;

    @Override
    public String toString() {
        return content + " (user=" + user + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(id, email.id) && Objects.equals(user, email.user) && Objects.equals(content, email.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, content);
    }
}

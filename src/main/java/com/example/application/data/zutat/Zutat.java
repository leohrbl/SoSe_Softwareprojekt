package com.example.application.data.zutat;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.application.data.einheit.Einheit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Zutat Entität mit einer ManyToOne Beziehung zu der Einheit Entität. Die Zutat
 * hat neben der Einheit einen Namen. Zutaten existieren nicht ohne Einheit. Die
 * Klasse hat Getter und Setter Methoden für alle Instanzvariablen.
 *
 * @author Léo Hérubel
 * @see Einheit
 * @see com.example.application.data.zutat.ZutatRepository
 * @see com.example.application.data.zutat.ZutatService
 */

@Entity
public class Zutat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @NotBlank
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "einheit_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Einheit einheit;

    public Zutat() {

    }

    public Einheit getEinheit() {
        return einheit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEinheit(Einheit einheit) {
        this.einheit = einheit;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

}
package com.example.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *  Zutat Entität mit einer ManyToOne Beziehung zu der Einheit Entität. Die Zutat hat neben der Einheit einen Namen. Zutaten existieren nicht ohne Einheit. Die Klasse hat Getter und Setter Methoden für alle Instanzvariablen.
 * @author Léo Hérubel
 * @see Einheit
 * @see com.example.application.data.repository.ZutatRepository
 * @see com.example.application.data.service.ZutatService
 */

@Entity
public class Zutat{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @NotBlank
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "einheit_id")
    private Einheit einheit;

    public Einheit getEinheit() {
        return einheit;
    }

    public Zutat()  {

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
}
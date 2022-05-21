package com.example.application.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotBlank;

/**
 * @author Philipp Laupichler
 *         Entität Einheit, mit zwei Instanzvariablen:
 *         einheit (Name) als String und ID als long, diese wird generiert
 *         Außerdem Kostruktoren, getter und setter
 */
@Entity
public class Einheit {
    @NotBlank
    @Column(unique = true)
    private String einheit;
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Einheit() {

    }

    public Einheit(String einheit) {
        this.einheit = einheit;
    }

    public Einheit(Long id, String einheit) {
        this.einheit = einheit;
        this.id = id;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.einheit;
    }
}

package com.example.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
/**
 * Entität für die Einkaufsliste mit many to many beziehung zu den Zutaten.
 * @author Joscha Cerny
 * @see Zutat
 * @see com.example.application.data.repository.EinkaufslistenRepository
 * @see com.example.application.data.service.EinkaufslistenService
 */

@Entity
public class EinkaufslistenEintrag {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; //Unique identifier für die Einkaufslisteneinträge

    private double menge; //Variable für die Menge der Zutaten

    @ManyToOne
    @NotNull
    private Zutat zutat; //Variable für den Fremdschlüssel der Entität Zutat


    public EinkaufslistenEintrag() //Standart Konstruktor
    {

    }

    public double getMenge()
    {
        return this.menge;
    }

    public void setMenge(Integer menge)
    {
        this.menge = menge;
    }

    public Zutat getZutat()
    {
        return this.zutat;
    }

    public void setZutat(Zutat zutat)
    {
        this.zutat = zutat;
    }

    public long getId()
    {
        return this.id;
    }
}

package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.application.data.repository.RezeptZutatenRepository;
import com.example.application.data.service.RezeptZutatenService;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.text.DecimalFormat;

/**
 * Rezept_Zutat Entität mit einer ManyToOne Beziehung zu der Repzept Entität und
 * einer ManyToOne Beziehung zu der Zutat Entität.
 * Die Rezept_Zutat Klasse hat eine id, ein Rezept, eine Zutat und die Menge.
 * Die Klasse hat Getter und Setter Methoden für alle
 * Instanzvariablen und eine toString Methode
 * 
 * @author Philipp Laupichler
 * @see Rezept
 * @see Zutat
 * @see RezeptZutatenRepository
 * @see RezeptZutatenService
 */
@Entity
public class Rezept_Zutat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "Rezept_id")
    private Rezept rezept;

    @ManyToOne
    @JoinColumn(name = "Zutat_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Zutat zutat;

    private double Menge;

    public Rezept_Zutat() {
    }

    public Rezept_Zutat(long id, Rezept rezept, Zutat zutat, double Menge) {
        this.id = id;
        this.rezept = rezept;
        this.zutat = zutat;
        this.Menge = Menge;
    }

    public Rezept_Zutat(Rezept rezept, Zutat zutat, double Menge) {

        this.rezept = rezept;
        this.zutat = zutat;
        this.Menge = Menge;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Rezept getRezept() {
        return this.rezept;
    }

    public void setRezept(Rezept rezept) {
        this.rezept = rezept;
    }

    public Zutat getZutat() {
        return this.zutat;
    }

    public void setZutat(Zutat zutat) {
        this.zutat = zutat;
    }

    public double getMenge() {
        return this.Menge;
    }

    public void setMenge(double Menge) {
        this.Menge = Menge;
    }

    public Rezept_Zutat id(long id) {
        setId(id);
        return this;
    }

    public Rezept_Zutat rezept(Rezept rezept) {
        setRezept(rezept);
        return this;
    }

    public Rezept_Zutat zutat(Zutat zutat) {
        setZutat(zutat);
        return this;
    }

    public Rezept_Zutat Menge(int Menge) {
        setMenge(Menge);
        return this;
    }

    public Einheit getEinheitFromZutat() {
        return this.getZutat().getEinheit();
    }

    /**
     * Gibt die Menge für das Frontend als Integer oder als Double in einem String zurück
     * @author Léo Hérubel
     * @return
     */
    public String getMengeString(){
        if((this.Menge % (int) this.Menge) == 0){
            return String.valueOf((int) this.Menge);
        } else {
            return new DecimalFormat("#.##").format(this.Menge);
        }
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", rezept='" + getRezept() + "'" +
                ", zutat='" + getZutat() + "'" +
                ", Menge='" + getMenge() + "'" +
                "}";
    }

}

package com.example.application.data.einkaufslisteneintrag;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.application.data.einheit.Einheit;
import com.example.application.data.zutat.Zutat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;

/**
 * Entität für die Einkaufsliste mit many to many beziehung zu den Zutaten.
 * 
 * @author Joscha Cerny
 * @see Zutat
 * @see com.example.application.data.einkaufslisteneintrag.EinkaufslistenRepository
 * @see com.example.application.data.einkaufslisteneintrag.EinkaufslistenService
 */

@Entity
public class EinkaufslistenEintrag {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; // Unique identifier für die Einkaufslisteneinträge

    private double menge; // Variable für die Menge der Zutaten

    @OneToOne
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private com.example.application.data.zutat.Zutat zutat; // Variable für den Fremdschlüssel der Entität Zutat

    public EinkaufslistenEintrag() // Standart Konstruktor
    {

    }

    /**
     * Gibt die Menge für das Frontend als Integer oder als Double in einem String
     * zurück
     * 
     * @author Léo Hérubel
     * @return
     */
    public String getMengeString() {
        if ((this.menge % (int) this.menge) == 0) {
            return String.valueOf((int) this.menge);
        } else {
            return new DecimalFormat("#.##").format(this.menge);
        }
    }

    public double getMenge() {
        return this.menge;
    }

    public void setMenge(double menge) {
        this.menge = menge;
    }

    public Zutat getZutat() {
        return this.zutat;
    }

    public void setZutat(Zutat zutat) {
        this.zutat = zutat;
    }

    public long getId() {
        return this.id;
    }

    public Einheit getEinheitByZutat() {
        return this.getZutat().getEinheit();
    }
}

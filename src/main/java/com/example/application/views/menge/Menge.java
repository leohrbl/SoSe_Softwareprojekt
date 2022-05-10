package com.example.application.views.menge;

import com.example.application.data.entity.Einheit;
import com.example.application.data.entity.Zutat;

public class Menge {

    private Einheit einheit;
    private Zutat zutat;
    private double menge;

    public Menge(Zutat zutat, Einheit einheit, double menge){
        this.zutat = zutat;
        this.einheit = einheit;
        this.menge = menge;
    }

    public Einheit getEinheit() {
        return einheit;
    }

    public Zutat getZutat() {
        return zutat;
    }

    public double getMenge() {
        return menge;
    }

    public void setEinheit(Einheit einheit) {
        this.einheit = einheit;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }

    public void setZutat(Zutat zutat) {
        this.zutat = zutat;
    }
}


package com.example.application.views.rezeptansicht;

import com.example.application.data.entity.EinkaufslistenEintrag;
import com.example.application.data.entity.Rezept_Zutat;

import java.util.LinkedList;
import java.util.List;

public class MengeService {

    public MengeService(){

    }

    public List<Menge> getMengenRezeptZutat(List<Rezept_Zutat> list){
        List<Menge> mengenList = new LinkedList<Menge>();
        for(Rezept_Zutat rezeptZutat: list){
            mengenList.add(new Menge(rezeptZutat.getZutat(), rezeptZutat.getZutat().getEinheit(), rezeptZutat.getMenge()));
        }
        return mengenList;
    }

    public List<Menge> getMengenEinkaufsliste(List<EinkaufslistenEintrag> list){
        List<Menge> mengenList = new LinkedList<Menge>();
        for(EinkaufslistenEintrag eintrag: list){
            mengenList.add(new Menge(eintrag.getZutat(), eintrag.getZutat().getEinheit(), eintrag.getMenge()));
        }
        return mengenList;
    }
}

package com.example.application.data.einkaufslisteneintrag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EinkaufslistenRepository extends JpaRepository<EinkaufslistenEintrag, Integer> {

    void deleteById(long id);

    EinkaufslistenEintrag findById(long id);
}
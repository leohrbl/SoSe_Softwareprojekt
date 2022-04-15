package org.vaadin.spring.tutorial;

import javax.persistence.*;

@Entity
@Table(name = "test_2")
public class KochbuchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    private String username;
    private int nummer;
 
    // getters and setters...
}




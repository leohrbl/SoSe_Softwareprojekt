package org.vaadin.spring.tutorial;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KochbuchRepository extends CrudRepository<KochbuchEntity, Integer>{
    
}

package com.focalid.crudoperations.repository;

import com.focalid.crudoperations.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> { // <Entity, Primary Key Type>

        Person findByFirstName(String firstName);
}

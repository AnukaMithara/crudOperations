package com.focalid.crudoperations.controller;

import com.focalid.crudoperations.entity.Person;
import com.focalid.crudoperations.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService service;

    @PostMapping("/addPerson")
    public Person addPerson(@RequestPart("person") Person person, @RequestPart("image") MultipartFile image) {
        return service.savePerson(person, image);
    }

    @GetMapping("/persons")
    public List<Person> findAllPersons() {
        return service.getPersons();
    }

    @GetMapping("/personById/{id}")
    public Person findPersonById(@PathVariable int id) {
        return service.getPersonById(id);
    }

    @GetMapping("/person/{firstName}")
    public Person findPersonByFirstName(@PathVariable String firstName) {
        return service.getPersonByFirstName(firstName);
    }

    @GetMapping("/image/{id}") // Get image by id
    public ResponseEntity<?> getImageById(@PathVariable int id) {
        byte[] image = service.getImageById(id);
        if (image != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/updatePerson/{id}")
    public Person updatePerson(@PathVariable int id, @RequestPart("person") Person person, @RequestPart(value = "image", required = false) MultipartFile image) {
        return service.updatePerson(id, person, image);
    }

    @DeleteMapping("/delete/{id}")
    public String deletePerson(@PathVariable int id) {
        return service.deletePerson(id);
    }
}


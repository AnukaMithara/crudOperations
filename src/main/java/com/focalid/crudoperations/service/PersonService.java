package com.focalid.crudoperations.service;
import com.focalid.crudoperations.entity.Person;
import com.focalid.crudoperations.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    private final String imageFolderPath = "src/main/resources/static/images/";

    public Person savePerson(Person person, MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
                person.setImageName(originalFilename);
                person.setImageType(image.getContentType());
                person.setImagePath(imageFolderPath + originalFilename);

                saveImage(image, person.getImagePath());
            }
            return repository.save(person);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Person> getPersons() {
        List<Person> persons = repository.findAll();
        for (Person person : persons) {
            String imagePath = person.getImagePath();
            try {
                byte[] imageData = getImageData(imagePath);
                person.setImageData(imageData);
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
                // You might want to set imageData to null or handle this case differently based on your requirements
            }
        }
        return persons;
    }

    public Person getPersonById(int id) {
        Optional<Person> personOptional = repository.findById(id);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            String imagePath = person.getImagePath();
            try {
                byte[] imageData = getImageData(imagePath);
                person.setImageData(imageData);
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
                // You might want to set imageData to null or handle this case differently based on your requirements
            }
            return person;
        } else {
            return null; // Or throw an exception, depending on your requirements
        }
    }

    public byte[] getImageById(int id) {
        Optional<Person> personOptional = repository.findById(id);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            String imagePath = person.getImagePath();
            try {
                return getImageData(imagePath);
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
                // You might want to return null or handle this case differently based on your requirements
            }
        }
        return null; // Or throw an exception, depending on your requirements
    }

    public Person getPersonByFirstName(String firstName) {
        Person person = repository.findByFirstName(firstName);
        if (person != null) {
            String imagePath = person.getImagePath();
            try {
                byte[] imageData = getImageData(imagePath);
                person.setImageData(imageData);
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
                // You might want to set imageData to null or handle this case differently based on your requirements
            }
        }
        return person;
    }

    public Person updatePerson(int id, Person person, MultipartFile image) {
        Person existingPerson = repository.findById(id).orElse(null);
        if (existingPerson != null) {
            existingPerson.setFirstName(person.getFirstName());
            existingPerson.setLastName(person.getLastName());
            existingPerson.setDescription(person.getDescription());
            existingPerson.setAge(person.getAge());
            existingPerson.setAddress(person.getAddress());
            existingPerson.setImageData(null);
            if (image != null && !image.isEmpty()) {
                existingPerson.setImageName(StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename())));
                existingPerson.setImageType(image.getContentType());
                existingPerson.setImagePath(imageFolderPath + existingPerson.getImageName());

                try {
                    saveImage(image, existingPerson.getImagePath());
                } catch (IOException e) {
                    e.printStackTrace(); // Handle or log the exception appropriately
                    // You might want to throw a custom exception or return null here
                    // depending on your application's error handling strategy
                    throw new RuntimeException(e);
                }
            }
            return repository.save(existingPerson);
        }
        return null;
    }

    public String deletePerson(int id) {
        Optional<Person> personOptional = repository.findById(id);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            String imagePath = person.getImagePath();
            try {
                deleteImage(imagePath);
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            }
        }
        repository.deleteById(id);
        return "Person removed !! " + id;
    }

    private void saveImage(MultipartFile image, String imagePath) throws IOException {
        Path destinationPath = Path.of(imagePath);
        Files.createDirectories(destinationPath.getParent());
        Files.copy(image.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private byte[] getImageData(String imagePath) throws IOException {
        Resource resource = new UrlResource(Path.of(imagePath).toUri());
        return Files.readAllBytes(resource.getFile().toPath());
    }

    private void deleteImage(String imagePath) throws IOException {
        Files.deleteIfExists(Path.of(imagePath));
    }
}

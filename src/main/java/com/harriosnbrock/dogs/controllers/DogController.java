package com.harriosnbrock.dogs.controllers;

import com.harriosnbrock.dogs.DogResourceAssembler;
import com.harriosnbrock.dogs.domain.Dog;
import com.harriosnbrock.dogs.respositories.DogRepository;
import org.apache.commons.text.WordUtils;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
@RestController
public class DogController {

    private final DogRepository repository;
    private final DogResourceAssembler assembler;

    public DogController(DogRepository repository, DogResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/dogs/breads")
    public Resources<Resource<Dog>> all() {
        List<Resource<Dog>> dogs = repository.findAll().stream()
                .sorted(Comparator.comparing(Dog::getBread))
                .map(assembler::toResource).collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/breeds/{name}")
    public Resources<Resource<Dog>> findByBreed(@PathVariable String name) {
        name = WordUtils.capitalizeFully(name);
        List<Resource<Dog>> dogs = repository.findByBread(name).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).findByBreed(name)).withSelfRel());
    }

    @GetMapping("/dogs/weight")
    public Resources<Resource<Dog>> allByWeight() {

        List<Resource<Dog>> dogs = repository.findAll().stream()
                .sorted(Comparator.comparing(Dog::getBread).reversed())
                .map(assembler::toResource).collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/apartment")
    public Resources<Resource<Dog>> byApartment() {
        List<Resource<Dog>> dogs = repository.findBySuitableApartmentIsTrue().stream()
                .map(assembler::toResource)
//                .filter(dog -> dog.getContent().isSuitableApartment())
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @PostMapping("/dogs")
    public ResponseEntity<?> addDog(@RequestBody Dog newDog) throws URISyntaxException {
        Dog dog = repository.save(newDog);
        Resource<Dog> resource = assembler.toResource(dog);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
    @PutMapping("/dogs/{id}")
    public ResponseEntity<?> replaceDog(@RequestBody Dog newDog, @PathVariable Long id) throws URISyntaxException {
        Dog updateDog = repository.findById(id)
                .map(dog -> {
                    dog.setBread(newDog.getBread());
                    dog.setWeight(newDog.getWeight());
                    dog.setSuitableApartment(newDog.isSuitableApartment());
                    return repository.save(dog);
                })
                .orElseGet(() ->{
                    newDog.setId(id);
                    return repository.save(newDog);
                });

        Resource<Dog> resource = assembler.toResource(updateDog);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);

    }

    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<?> deleteDogById(@PathVariable Long id){
        Optional<Dog> dog = repository.findById(id);
        if (dog.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/dogs/bread/{bread}")
    public ResponseEntity<?> deleteDogByBread(@PathVariable String bread){

        bread = WordUtils.capitalizeFully(bread);
//        List<Dog> dogs =  repository.findByBread(bread);
//        System.out.println(dogs.get(0).getBread());
        System.out.println("delete by bread");
        repository.deleteByBread(bread);
        System.out.println("deleted");
        return ResponseEntity.noContent().build();
    }


}

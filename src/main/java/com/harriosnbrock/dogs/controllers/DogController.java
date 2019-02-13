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

    @GetMapping("/dogs/breeds")
    public Resources<Resource<Dog>> all() {
        List<Resource<Dog>> dogs = repository.findAll().stream()
                .map(assembler::toResource)
                .sorted((d11, d12) ->
                        d11.getContent().getBread().compareToIgnoreCase(d12.getContent().getBread()))
                .collect(Collectors.toList());
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

}

package com.harriosnbrock.dogs.controllers;

import com.harriosnbrock.dogs.DogResourceAssembler;
import com.harriosnbrock.dogs.domain.Dog;
import com.harriosnbrock.dogs.respositories.DogRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
                .map(assembler::toResource).sorted((d11, d12) ->
                        d11.getContent().getBread().compareToIgnoreCase(d12.getContent().getBread())).collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

//    @GetMapping("/dogs/breeds/{name}")
//    public Resource<Dog> findOne(@PathVariable String name) {
//        Dog dog = repository.findOne().orElse(null);
//        return assembler.toResource(dog);
//    }

    @GetMapping("/dogs/weight")
    public Resources<Resource<Dog>> allByWeight() {
        List<Resource<Dog>> dogs = repository.findAll().stream()
                .map(assembler::toResource).sorted((d11, d12) ->
                        d12.getContent().getWeight() - (d11.getContent().getWeight())).collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/apartment")
    public Resources<Resource<Dog>> byApartment() {
        List<Resource<Dog>> dogs = repository.findAll().stream()
                .map(assembler::toResource)
                .filter(dog -> dog.getContent().isSuitableApartment() == true)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

}

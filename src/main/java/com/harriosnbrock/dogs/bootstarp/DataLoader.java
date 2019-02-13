package com.harriosnbrock.dogs.bootstarp;

import com.harriosnbrock.dogs.domain.Dog;
import com.harriosnbrock.dogs.respositories.DogRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private DogRepository dogRepository;

    public DataLoader(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
        dogRepository.save(new Dog("Springer", 50, false));
        dogRepository.save(new Dog("Bulldog", 50, true));
        dogRepository.save(new Dog("Collie", 50, false));
        dogRepository.save(new Dog("Boston Terri", 35, true));
        dogRepository.save(new Dog("Corgie", 35, true));
    }
}

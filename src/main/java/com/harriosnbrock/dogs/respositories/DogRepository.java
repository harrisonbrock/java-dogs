package com.harriosnbrock.dogs.respositories;

import com.harriosnbrock.dogs.domain.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long> {
    List<Dog> findByBread(String name);
    List<Dog> findBySuitableApartmentIsTrue();
    @Transactional
    void deleteByBread(String bread);

}

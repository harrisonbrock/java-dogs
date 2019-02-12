package com.harriosnbrock.dogs.respositories;

import com.harriosnbrock.dogs.domain.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
}

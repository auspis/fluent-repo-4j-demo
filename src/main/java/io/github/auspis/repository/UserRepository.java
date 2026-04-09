package io.github.auspis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.github.auspis.domain.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    // Inherited: save(), findById(), findAll(), count(), deleteById(), ...

    List<User> findByEmailIgnoreCase(String email);
    List<User> findByNameContainingIgnoreCase(String name);
    List<User> findByAgeGreaterThan(Integer minAge);
}

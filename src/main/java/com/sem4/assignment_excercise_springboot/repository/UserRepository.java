package com.sem4.assignment_excercise_springboot.repository;

import com.sem4.assignment_excercise_springboot.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    @EntityGraph(value = "User.withDepartment", type = EntityGraph.EntityGraphType.LOAD)
    List<User> findAll();

    @Override
    @EntityGraph(value = "User.withDepartment", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findById(Long id);

    @EntityGraph(value = "User.withDepartment", type = EntityGraph.EntityGraphType.LOAD)
    List<User> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Optional<User> findByName(String name);
}

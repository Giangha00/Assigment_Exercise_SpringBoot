package com.sem4.assignment_excercise_springboot.config;

import com.sem4.assignment_excercise_springboot.entity.Department;
import com.sem4.assignment_excercise_springboot.entity.User;
import com.sem4.assignment_excercise_springboot.repository.DepartmentRepository;
import com.sem4.assignment_excercise_springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            Department dev = departmentRepository.save(new Department("Development"));
            Department hr = departmentRepository.save(new Department("Human Resources"));
            Department finance = departmentRepository.save(new Department("Finance"));

            if (userRepository.count() == 0) {
                userRepository.saveAll(List.of(
                        new User("Sam", 30, 70000.0, dev),
                        new User("Tom", 40, 50000.0, finance),
                        new User("Bob", 30, 12000.0, hr),
                        new User("Silvia", 42, 15000.0, dev)
                ));
            }
        }
    }
}

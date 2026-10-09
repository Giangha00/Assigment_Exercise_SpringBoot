package com.sem4.assignment_excercise_springboot.service.impl;

import com.sem4.assignment_excercise_springboot.entity.Department;
import com.sem4.assignment_excercise_springboot.exception.ResourceNotFoundException;
import com.sem4.assignment_excercise_springboot.repository.DepartmentRepository;
import com.sem4.assignment_excercise_springboot.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    @Override
    @Transactional
    public Department getOrCreateDefaultDepartment() {
        return departmentRepository.findByName("General")
                .orElseGet(() -> departmentRepository.save(new Department("General")));
    }
}

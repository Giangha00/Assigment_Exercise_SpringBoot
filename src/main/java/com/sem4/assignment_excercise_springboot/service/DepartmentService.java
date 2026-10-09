package com.sem4.assignment_excercise_springboot.service;

import com.sem4.assignment_excercise_springboot.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    Department getOrCreateDefaultDepartment();
}

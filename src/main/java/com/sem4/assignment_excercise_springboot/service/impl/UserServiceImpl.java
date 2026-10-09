package com.sem4.assignment_excercise_springboot.service.impl;

import com.sem4.assignment_excercise_springboot.dto.UserFormDto;
import com.sem4.assignment_excercise_springboot.entity.Department;
import com.sem4.assignment_excercise_springboot.entity.User;
import com.sem4.assignment_excercise_springboot.exception.DuplicateUserNameException;
import com.sem4.assignment_excercise_springboot.exception.ResourceNotFoundException;
import com.sem4.assignment_excercise_springboot.repository.UserRepository;
import com.sem4.assignment_excercise_springboot.service.DepartmentService;
import com.sem4.assignment_excercise_springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DepartmentService departmentService;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public User saveUser(UserFormDto formDto) {
        Department department;
        if (formDto.getDepartmentId() != null) {
            department = departmentService.getDepartmentById(formDto.getDepartmentId());
        } else {
            department = departmentService.getOrCreateDefaultDepartment();
        }

        if (formDto.getId() == null) {
            // Create user
            if (userRepository.existsByName(formDto.getName().trim())) {
                throw new DuplicateUserNameException("Unable to create. A User with name " + formDto.getName().trim() + " already exist");
            }

            User user = User.builder()
                    .name(formDto.getName().trim())
                    .age(formDto.getAge())
                    .salary(formDto.getSalary())
                    .department(department)
                    .build();

            return userRepository.save(user);
        } else {
            // Update user
            User existingUser = getUserById(formDto.getId());

            if (userRepository.existsByNameAndIdNot(formDto.getName().trim(), formDto.getId())) {
                throw new DuplicateUserNameException("Unable to update. A User with name " + formDto.getName().trim() + " already exist");
            }

            existingUser.setName(formDto.getName().trim());
            existingUser.setAge(formDto.getAge());
            existingUser.setSalary(formDto.getSalary());
            existingUser.setDepartment(department);

            return userRepository.save(existingUser);
        }
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll();
        }
        return userRepository.findByNameContainingIgnoreCase(keyword.trim());
    }

    @Override
    public UserFormDto convertToDto(User user) {
        return UserFormDto.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .salary(user.getSalary())
                .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                .build();
    }
}

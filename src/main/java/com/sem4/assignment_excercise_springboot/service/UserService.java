package com.sem4.assignment_excercise_springboot.service;

import com.sem4.assignment_excercise_springboot.dto.UserFormDto;
import com.sem4.assignment_excercise_springboot.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User saveUser(UserFormDto formDto);
    void deleteUser(Long id);
    List<User> searchUsers(String keyword);
    UserFormDto convertToDto(User user);
}

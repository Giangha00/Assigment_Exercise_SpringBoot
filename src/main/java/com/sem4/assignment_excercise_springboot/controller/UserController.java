package com.sem4.assignment_excercise_springboot.controller;

import com.sem4.assignment_excercise_springboot.dto.UserFormDto;
import com.sem4.assignment_excercise_springboot.entity.User;
import com.sem4.assignment_excercise_springboot.exception.DuplicateUserNameException;
import com.sem4.assignment_excercise_springboot.service.DepartmentService;
import com.sem4.assignment_excercise_springboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping({"/", "/users"})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final DepartmentService departmentService;

    @GetMapping
    public String index(Model model) {
        if (!model.containsAttribute("userForm")) {
            model.addAttribute("userForm", new UserFormDto());
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("isEdit", false);
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("userForm", userService.convertToDto(user));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("isEdit", true);
        return "index";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("userForm") UserFormDto formDto,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        boolean isUpdate = formDto.getId() != null;

        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("isEdit", isUpdate);
            return "index";
        }

        try {
            userService.saveUser(formDto);
            String successMsg = isUpdate ? "User updated successfully" : "User created successfully";
            redirectAttributes.addFlashAttribute("successMessage", successMsg);
            return "redirect:/users";
        } catch (DuplicateUserNameException ex) {
            String errorMsg = isUpdate
                    ? "Error while updating User: " + ex.getMessage()
                    : "Error while creating User: " + ex.getMessage();
            model.addAttribute("errorMessage", errorMsg);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("isEdit", isUpdate);
            return "index";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error while deleting User: " + ex.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<User> results = userService.searchUsers(keyword);
        model.addAttribute("users", results);
        model.addAttribute("keyword", keyword);
        model.addAttribute("userForm", new UserFormDto());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("isEdit", false);
        return "index";
    }
}

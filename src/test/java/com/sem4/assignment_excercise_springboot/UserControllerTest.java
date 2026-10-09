package com.sem4.assignment_excercise_springboot;

import com.sem4.assignment_excercise_springboot.controller.UserController;
import com.sem4.assignment_excercise_springboot.entity.User;
import com.sem4.assignment_excercise_springboot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testIndexPageLoadsWithUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("userForm"))
                .andExpect(model().attribute("isEdit", false));
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        mockMvc.perform(post("/users/save")
                        .param("name", "Alice")
                        .param("age", "25")
                        .param("salary", "45000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute("successMessage", "User created successfully"));

        assertTrue(userRepository.existsByName("Alice"));
    }

    @Test
    void testCreateDuplicateUserShowsErrorMessage() throws Exception {
        // Attempt to create "Bob" which already exists
        mockMvc.perform(post("/users/save")
                        .param("name", "Bob")
                        .param("age", "67")
                        .param("salary", "67000"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("errorMessage", containsString("Error while creating User: Unable to create. A User with name Bob already exist")));
    }

    @Test
    void testSearchUsers() throws Exception {
        mockMvc.perform(get("/users/search").param("keyword", "Silvia"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("users", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testEditUserPage() throws Exception {
        User bob = userRepository.findByName("Bob").orElseThrow();
        mockMvc.perform(get("/users/edit/" + bob.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("isEdit", true))
                .andExpect(model().attribute("userForm", hasProperty("name", is("Bob"))));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Create temporary user to delete
        mockMvc.perform(post("/users/save")
                        .param("name", "TempUser")
                        .param("age", "33")
                        .param("salary", "30000"))
                .andExpect(status().is3xxRedirection());

        User tempUser = userRepository.findByName("TempUser").orElseThrow();

        mockMvc.perform(get("/users/delete/" + tempUser.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute("successMessage", "User deleted successfully"));

        assertFalse(userRepository.existsByName("TempUser"));
    }
}

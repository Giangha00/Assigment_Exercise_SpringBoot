package com.sem4.assignment_excercise_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@NamedEntityGraph(
    name = "User.withDepartment",
    attributeNodes = @NamedAttributeNode("department")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    public User(String name, Integer age, Double salary, Department department) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.department = department;
    }
}

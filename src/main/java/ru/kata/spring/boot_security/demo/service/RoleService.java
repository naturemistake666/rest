package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface RoleService {
    List<Role> findAllRole();
    Set<Role> findByIdRoles(List<Long> roles);
    void addRole(ArrayList<Long> roles, User user);
}

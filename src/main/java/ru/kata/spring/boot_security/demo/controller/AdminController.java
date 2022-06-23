package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.ArrayList;


@Controller
@RequestMapping()
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleService roleService) {
        this.userService = userServiceImpl;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String findAll(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAllRole());
        return "user-list";
    }

    @GetMapping("/admin/user-create")
    public String createUserForm(Model model,@AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        model.addAttribute("listRoles", roleService.findAllRole());
        return "user-create";
    }

    @PostMapping("add")
    public String createUser(@RequestParam("role") ArrayList<Long> roles, @ModelAttribute("userNew") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user-create";
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            bindingResult.addError(new FieldError("username", "username",
                    String.format("User with email \"%s\" is already exist!", user.getEmail())));
            return "user-create";
        }
        roleService.addRole(roles,user);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/user-update/{id}")
    public String updateUser(@RequestParam("role")ArrayList<Long> roles, User user, @PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("listRoles", roleService.findAllRole());
            roleService.addRole(roles, user);
            userService.saveUser(user);
            return "redirect:/admin";
    }


}

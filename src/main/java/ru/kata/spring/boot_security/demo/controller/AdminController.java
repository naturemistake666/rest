package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

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
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }
    @GetMapping("/admin/user-create")
    public String createUserForm(User user, Model model) {
        model.addAttribute("listRoles", roleService.findAllRole());
        return "user-create";
    }

    @PostMapping("/admin/user-create")
    public String createUser(@RequestParam("role")ArrayList<Long> roles, @ModelAttribute("user") User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "user-create";
        }
        if (userService.findByEmail(user.getEmail()) != null){
            bindingResult.addError(new FieldError("username", "username",
                    String.format("User with email \"%s\" is already exist!", user.getEmail())));
            return "user-create";
        }
        user.setRoles(roleService.findByIdRoles(roles));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("listRoles", roleService.findAllRole());
        return "user-update";
    }

    @PostMapping("/admin/user-update")
   public String updateUser(@RequestParam("role")ArrayList<Long> roles,  User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "user-update";
        } else {
            user.setRoles((roleService.findByIdRoles(roles)));
            userService.saveUser(user);
            return "redirect:/admin";
        }
   }
}
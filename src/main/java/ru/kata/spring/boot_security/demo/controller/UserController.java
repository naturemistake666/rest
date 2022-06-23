package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/")
public class UserController {

    @GetMapping(value = "login")
    public String loginPage() {
        return "login";
    }
   @GetMapping("/")
   public String startPage() {

       return "index";
    }
}

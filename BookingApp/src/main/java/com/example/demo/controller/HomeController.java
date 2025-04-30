package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String showHomePage(Model model) {
        String username = "山田太郎"; // 仮の表示用ユーザー名
        model.addAttribute("username", username);
        return "home";
    }
}

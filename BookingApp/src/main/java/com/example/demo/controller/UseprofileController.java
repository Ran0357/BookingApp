package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Controller
public class UseprofileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/userprofile")
    public String showGymStatus(Model model) {
        List<User> machines = userRepository.findByUserid("user001"); // ← 特定のuseridのみ取得
        model.addAttribute("machines", machines);
        return "userprofile";
    }

    @PostMapping("/userprofile/update")
    public String updateUser(User formUser) {
        // 入力されたIDで既存ユーザーを取得
        User user = userRepository.findById(formUser.getId()).orElse(null);

        if (user != null) {
            // usertype = 1 のときだけ名前が更新可能
            if (user.getUsertype() == 1) {
                user.setUsername(formUser.getUsername());
            }

            // パスワードは常に更新可能
            user.setPassword(formUser.getPassword());

            userRepository.save(user);
        }

        return "redirect:/userprofile";
    }
    @GetMapping("/school/notifications")
    public String showNotifications() {
        return "notifications";  // templates/notifications.html を返すだけ
    }
    

}



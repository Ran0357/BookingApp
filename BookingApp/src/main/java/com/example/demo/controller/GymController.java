package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Gym;
import com.example.demo.repository.GymRepository;

@Controller
public class GymController {

    @Autowired
    private GymRepository gymRepository;

    @GetMapping("/gym")
    public String showGymStatus(Model model) {
        List<Gym> machines = gymRepository.findAll();
        model.addAttribute("machines", machines);
        return "gym";
    }

    @PostMapping("/gym/start")
    public String startUse(@RequestParam String managementnumber) {
        Gym gym = gymRepository.findById(managementnumber).orElseThrow();
        gym.setUsagestatus(1);
        gymRepository.save(gym);
        return "redirect:/gym"; // ← ここ
    }

    @PostMapping("/gym/stop")
    public String stopUse(@RequestParam String managementnumber) {
        Gym gym = gymRepository.findById(managementnumber).orElseThrow();
        gym.setUsagestatus(0);
        gymRepository.save(gym);
        return "redirect:/gym"; // ← ここ
    }

}

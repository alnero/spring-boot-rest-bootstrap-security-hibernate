package com.example.project.controller;

import com.example.project.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminController {
    @GetMapping()
    public String adminPage(ModelMap model, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        Long authenticatedUserId = authenticatedUser.getId();
        model.addAttribute("userId", authenticatedUserId);
        return "admin";
    }

    @GetMapping("/edit")
    public String editModalPage(ModelMap model) {
        model.addAttribute("action", "edit");
        return "modal";
    }

    @GetMapping("/delete")
    public String deleteModalPage(ModelMap model) {
        model.addAttribute("action", "delete");
        return "modal";
    }
}

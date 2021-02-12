package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.model.UserAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersController {
    @GetMapping()
    public String adminPage(ModelMap model, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        Long authenticatedUserId = authenticatedUser.getId();
        model.addAttribute("userId", authenticatedUserId);
        return "admin";
    }

    @GetMapping("/{id}")
    public String userPage(@PathVariable Long id, ModelMap model, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        Long authenticatedUserId = authenticatedUser.getId();
        String authenticatedUserAuthority = authenticatedUser.getAuthorities().iterator().next().getAuthority();
        // admin has access to any user page
        if (UserAuthority.Role.ADMIN.name().equals(authenticatedUserAuthority) && !id.equals(authenticatedUserId)) {
            model.addAttribute("userId", id);
            return "user";
        }
        // user has access only to his own page and re-directed to his own page when trying to access pages of other users
        if (id.equals(authenticatedUserId)) {
            model.addAttribute("userId", authenticatedUserId);
            return "user";
        } else {
            return "redirect:/users/" + authenticatedUserId;
        }
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

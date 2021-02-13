package com.example.project.controller;

import com.example.project.model.Role;
import com.example.project.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping()
    public String userPage(@RequestParam Long id, ModelMap model, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        Long authenticatedUserId = authenticatedUser.getId();
        String authenticatedUserRole = authenticatedUser.getAuthorities().iterator().next().getAuthority();
        // admin has access to any user page
        if (Role.AvailableRoles.ADMIN.name().equals(authenticatedUserRole) && !id.equals(authenticatedUserId)) {
            model.addAttribute("userId", id);
            return "user";
        }
        // user has access only to his own page and re-directed to his own page when trying to access pages of other users
        if (id.equals(authenticatedUserId)) {
            model.addAttribute("userId", authenticatedUserId);
            return "user";
        } else {
            return "redirect:/user?id=" + authenticatedUserId;
        }
    }
}

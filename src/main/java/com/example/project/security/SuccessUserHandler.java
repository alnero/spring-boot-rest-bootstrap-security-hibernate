package com.example.project.security;

import com.example.project.model.Role;
import com.example.project.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        User authenticatedUser = (User) authentication.getPrincipal();
        Long id = authenticatedUser.getId();
        if (roles.contains(Role.AvailableRoles.ADMIN.name())) {
            httpServletResponse.sendRedirect("/users");
        } else if (roles.contains(Role.AvailableRoles.USER.name())){
            httpServletResponse.sendRedirect("/users/" + id);
        }
    }
}

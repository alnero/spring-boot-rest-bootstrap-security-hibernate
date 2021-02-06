package com.example.project.security;

import com.example.project.model.User;
import com.example.project.model.UserAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Long id = currentAuthenticatedUser().getId();
        if (roles.contains(UserAuthority.Role.ADMIN.name())) {
            httpServletResponse.sendRedirect("/users");
        } else if (roles.contains(UserAuthority.Role.USER.name())){
            httpServletResponse.sendRedirect("/users/" + id);
        }
    }

    public User currentAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (User) authentication.getPrincipal();
    }
}

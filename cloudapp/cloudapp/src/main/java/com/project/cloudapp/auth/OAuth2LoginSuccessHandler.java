package com.project.cloudapp.auth;

import com.project.cloudapp.people.People;
import com.project.cloudapp.people.PeopleRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final PeopleRepo peopleRepo;

    @Value("${spring.frontend.redirectUrl}")
    private String redirectUrl;



    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull Authentication authentication) throws IOException {
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        String email=user.getAttribute("email");
        var people = peopleRepo.findByEmail(email).orElseGet(() -> {
            People newUser = new People();
            newUser.setEmail(email);
            newUser.setName(user.getAttribute("name"));
            newUser.setPassword(user.getAttribute("password"));
            return peopleRepo.save(newUser);
        });

        String token = jwtService.generateToken(people);
        response.sendRedirect(redirectUrl+"?token="+token);
    }
}

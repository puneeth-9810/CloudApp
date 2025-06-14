package com.project.cloudapp.auth;

import com.project.cloudapp.people.People;
import com.project.cloudapp.people.PeopleRepo;
import com.project.cloudapp.people.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final PeopleRepo peopleRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegisterRequest request) {
        // Check if user already exists
        if (peopleRepo.findByEmail(request.getEmail()).isPresent()) {
            return AuthenticationResponse.builder().token(null).build();
        }

        // Save new user with encoded password
        var people = People.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // ✅ Encode password
                .role(Role.USER)
                .build();

        peopleRepo.save(people);

        // Generate token
        var jwtToken = jwtService.generateToken(people);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var optionalUser = peopleRepo.findByEmail(request.getEmail());

        // If user not found
        if (optionalUser.isEmpty()) {
            return AuthenticationResponse.builder().token(null).build();
        }

        var people = optionalUser.get();

        // Compare raw password with encoded password
        if (!passwordEncoder.matches(request.getPassword(), people.getPassword())) {
            return AuthenticationResponse.builder().token(null).build();
        }

        // Generate JWT token
        var jwtToken = jwtService.generateToken(people);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}

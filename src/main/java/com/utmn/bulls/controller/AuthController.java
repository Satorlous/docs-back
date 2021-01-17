package com.utmn.bulls.controller;

import com.utmn.bulls.exception.BadRequestException;
import com.utmn.bulls.models.AuthProvider;
import com.utmn.bulls.models.User;
import com.utmn.bulls.payload.ApiResponse;
import com.utmn.bulls.payload.AuthResponse;
import com.utmn.bulls.payload.LoginRequest;
import com.utmn.bulls.payload.SignUpRequest;
import com.utmn.bulls.repository.CountryRepo;
import com.utmn.bulls.repository.RoleRepo;
import com.utmn.bulls.repository.UserRepo;
import com.utmn.bulls.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CountryRepo countryRepo;

    @Autowired
    private RoleRepo roleRepo;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepo.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Данный E-Mail уже используется!");
        }

        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setCountry(countryRepo.findByName(signUpRequest.getCountry()));
        user.setProvider(AuthProvider.local);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(roleRepo.findByName("user"));

        User result = userRepo.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Регистрация успешна!"));
    }

}

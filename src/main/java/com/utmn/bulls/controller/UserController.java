package com.utmn.bulls.controller;

import com.utmn.bulls.exception.ResourceNotFoundException;
import com.utmn.bulls.models.Country;
import com.utmn.bulls.models.Event;
import com.utmn.bulls.models.User;
import com.utmn.bulls.repository.CountryRepo;
import com.utmn.bulls.repository.EventRepo;
import com.utmn.bulls.repository.RoleRepo;
import com.utmn.bulls.repository.UserRepo;
import com.utmn.bulls.security.CurrentUser;
import com.utmn.bulls.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;


@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CountryRepo countryRepo;


    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepo.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public Country getC() {
        return countryRepo.findByCode2("ru");
    }
}

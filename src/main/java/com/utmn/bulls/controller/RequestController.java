package com.utmn.bulls.controller;

import com.utmn.bulls.exception.ResourceNotFoundException;
import com.utmn.bulls.model.Result;
import com.utmn.bulls.model.User;
import com.utmn.bulls.repository.ResultRepository;
import com.utmn.bulls.repository.UserRepository;
import com.utmn.bulls.requestmodel.ResultRequest;
import com.utmn.bulls.security.CurrentUser;
import com.utmn.bulls.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/result/")
public class RequestController {

    @Autowired
    private ResultRepository resultRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("me")
    public List<Result> getResultsByMe(@CurrentUser UserPrincipal userPrincipal) {
        List<Result> list = resultRepo.findByUser(getCurrentUser(userPrincipal));
        list.sort((a, b) -> a.getAttempts() < b.getAttempts() ? -1 : 0);
        return list;
    }

    @GetMapping("all")
    public List<Result> getAllResults() {
        List<Result> list = resultRepo.findAll();
        list.sort((a, b) -> a.getAttempts() < b.getAttempts() ? -1 : 0);
        return list;
    }

    @PostMapping("add")
    @PreAuthorize("hasRole('USER')")
    public void addResult(@RequestBody ResultRequest req, @CurrentUser UserPrincipal userPrincipal) {
        Result res = new Result(
                req.getNumber(), req.getAttempts(),
                getCurrentUser(userPrincipal));
        resultRepo.save(res);
    }

    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepo.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    //
    @PostMapping("{userId}")
    public List<Result> getResultsByUserId(@PathVariable Long userId) {
        return resultRepo.findByUserId(userId);
    }
}

package com.utmn.bulls.controller;

import com.utmn.bulls.models.Country;
import com.utmn.bulls.repository.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {

    @Autowired
    private CountryRepo countryRepo;

    @PostMapping("/all")
    public List<Country> getAll() {
        return countryRepo.findAll();
    }

    @PostMapping("/name")
    public Country name(String name) {
        return countryRepo.findByName(name);
    }

    @GetMapping("/like/{name}")
    public List<Country> like(@PathVariable("name") String name) {
        return countryRepo.findByNameContaining(name);
    }

}

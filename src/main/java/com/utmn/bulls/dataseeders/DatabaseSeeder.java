package com.utmn.bulls.dataseeders;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.utmn.bulls.models.*;
import com.utmn.bulls.repository.*;
import com.utmn.bulls.util.ERole;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.*;

@Component
public class DatabaseSeeder {
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private DocumentRepo docRepo;

    @Autowired
    private CountryRepo countryRepo;

    @Autowired
    private Flyway flyway;

    @EventListener
    public void seed(ContextRefreshedEvent event) {

        executeFlywayMigrations();

        if(roleRepo.count() == 0) {
            seedRolesTable();
        }

        if (eventRepo.count() == 0) {
            seedEventsTable();
        }

        if(userRepo.count() == 0) {
            seedUsersTable();
        }

        if(docRepo.count() == 0) {
            seedDocumentsTable();
        }


    }

    private void seedRolesTable() {
        roleRepo.saveAll(Arrays.asList(
            new Role("user", ERole.User.getLevel()),
            new Role("expert",ERole.Expert.getLevel()),
            new Role("admin", ERole.Admin.getLevel())
        ));
    }

    private void seedUsersTable() {
        Set<Event> events = new HashSet<>(
                Arrays.asList(eventRepo.getOne(1L))
        );

        for (int i = 1; i <= 4; i++) {
            User user = new User();
            user.setFirstName("qwe" + i);
            user.setLastName("qwe" + i);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setEmail("mail"+ i +"@mail.ru");
            user.setCountry(countryRepo.findByCode2("ru"));
            user.setRole(i <= 2 ? roleRepo.findByName("user") : i == 3 ? roleRepo.findByName("expert") : roleRepo.findByName("admin"));
            user.setProvider(AuthProvider.local);
            user.setEvents(events);
            userRepo.saveAndFlush(user);
        }

    }

    private void seedEventsTable() {
        Event event = new Event();
        event.setcDate(Date.valueOf("2020-12-31"));
        event.setCplusDate(Date.valueOf("2020-12-31"));
        event.setFinishDate(Date.valueOf("2020-12-31"));
        event.setStartDate(Date.valueOf("2020-12-31"));
        event.setName("Test Event #1");
        eventRepo.saveAndFlush(event);
    }

    private void seedDocumentsTable() {
        for (int i = 1; i <= 4; i++) {
            Document doc = new Document();
            doc.setDay("c1");
            doc.setRole(i <= 2 ? roleRepo.findByName("user") : roleRepo.findByName("expert"));
            doc.setEvent(eventRepo.findByName("Test Event #1"));
            doc.setName("docname"+i);
            doc.setContent("doccontent"+i);
            docRepo.saveAndFlush(doc);
        }
    }

    private void executeFlywayMigrations() {
        flyway.migrate();
    }

}

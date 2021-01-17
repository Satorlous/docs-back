package com.utmn.bulls.repository;

import com.utmn.bulls.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event, Long> {

    Event findByName(String name);
}

package com.utmn.bulls.repository;

import com.utmn.bulls.models.Document;
import com.utmn.bulls.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepo extends JpaRepository<Document, Long> {

    Document findByName(String name);
}

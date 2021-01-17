package com.utmn.bulls.repository;

import com.utmn.bulls.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountryRepo extends JpaRepository<Country, Long> {

    Country findByName(String name);

    Country findByCode2(String code2);

    Country findByCode3(String code3);

    List<Country> findByNameStartingWithIgnoreCase(String name);

    @Query("select countries.id, countries.code2, countries.code3, countries.name from Country countries  where countries.name like %:name%")
    List<Country> findByNameContainingMy(@Param("name") String name);

    List<Country> findByNameContaining(String name);

    List<Country> findByCode3Containing(String name);
}

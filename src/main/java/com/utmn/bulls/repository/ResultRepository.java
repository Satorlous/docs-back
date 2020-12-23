package com.utmn.bulls.repository;

import com.utmn.bulls.model.Result;
import com.utmn.bulls.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByUser(User user);

    List<Result> findByUserId(Long id);

}

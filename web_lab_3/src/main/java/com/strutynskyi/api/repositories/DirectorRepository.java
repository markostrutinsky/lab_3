package com.strutynskyi.api.repositories;

import com.strutynskyi.api.models.Director;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
    @Query("SELECT COUNT(d) > 0 FROM Director d WHERE d.firstName = :firstName AND d.lastName = :lastName")
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}

package com.strutynskyi.api.services.interfaces;

import com.strutynskyi.api.dto.director.CreateDirectorRequestDTO;
import com.strutynskyi.api.dto.director.UpdateDirectorRequestDTO;
import com.strutynskyi.api.models.Director;

import java.util.List;

public interface DirectorService {
    List<Director> findAll();
    Director findById(Long id);
    Director save(CreateDirectorRequestDTO director);
    Director update(UpdateDirectorRequestDTO director, Long id);
    Director delete(Long id);
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}

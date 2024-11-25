package com.strutynskyi.api.services.interfaces;

import com.strutynskyi.api.dto.movie.CreateMovieRequestDTO;
import com.strutynskyi.api.dto.movie.UpdateMovieRequestDTO;
import com.strutynskyi.api.models.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> findAll();
    Movie findById(Long id);
    List<Movie> findByDirector(String firstName, String lastName);
    Movie save(CreateMovieRequestDTO movie);
    Movie update(Long id , UpdateMovieRequestDTO movie);
    Movie delete(Long id);
}

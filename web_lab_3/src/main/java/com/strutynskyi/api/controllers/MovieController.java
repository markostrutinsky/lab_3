package com.strutynskyi.api.controllers;

import com.strutynskyi.api.dto.movie.CreateMovieRequestDTO;
import com.strutynskyi.api.dto.movie.MovieDTO;
import com.strutynskyi.api.dto.movie.MovieResponseDTO;
import com.strutynskyi.api.dto.movie.UpdateMovieRequestDTO;
import com.strutynskyi.api.mappers.MovieMappers;
import com.strutynskyi.api.models.Movie;
import com.strutynskyi.api.services.interfaces.MovieService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private static final Logger logger = LogManager.getLogger("project");

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAll() {
        logger.info("MovieController:getAll() Received request to fetch all movies");
        List<MovieDTO> movies = movieService.findAll()
                .stream()
                .map(MovieMappers::toMovieDTO)
                .toList();
        logger.info("MovieController:getAll() Fetched {} movies", movies.size());
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getById(@PathVariable Long id) {
        logger.info("MovieController:getById() Received request to fetch director by id: {}", id);
        Movie existingMovie = movieService.findById(id);
        MovieDTO movieDTO = MovieMappers.toMovieDTO(existingMovie);
        logger.info("MovieController:getById() Successfully fetched movie by id: {}", id);
        return ResponseEntity.ok(movieDTO);
    }

    @GetMapping("/by-director")
    public ResponseEntity<List<MovieResponseDTO>> getByDirector(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("MovieController:getByDirector() Fetching movies by director: {} {}", firstName, lastName);
        List<Movie> movies = movieService.findByDirector(firstName, lastName);
        List<MovieResponseDTO> moviesResponseDTO = movies
                .stream()
                .map(MovieMappers::toMovieResponseDTOFromMovie)
                .toList();
        logger.info("MovieController:getByDirector() Successfully fetched {} movies", movies.size());
        return new ResponseEntity<>(moviesResponseDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createMovie(@RequestBody CreateMovieRequestDTO createDTO) {
        logger.info("MovieController:createMovie() Received request to create movie");
        Movie saved = movieService.save(createDTO);
        logger.info("MovieController:createMovie() Created movie with id: {}", saved.getId());
        return new ResponseEntity<>("New movie was created with id: " + saved.getId(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long id, @RequestBody UpdateMovieRequestDTO updateDTO) {
        logger.info("MovieController:updateMovie() Received request to update movie by id: {}", id);
        Movie updatedMovie = movieService.update(id, updateDTO);
        MovieDTO movieDTO = MovieMappers.toMovieDTO(updatedMovie);
        logger.info("MovieController:updateMovie() Successfully updated movie: {}", movieDTO);
        return new ResponseEntity<>(movieDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        logger.info("MovieController:deleteMovie() Received request to delete movie by id: {}", id);
        Movie deletedMovie = movieService.delete(id);
        logger.info("MovieController:deleteMovie() Successfully deleted movie by id: {}", id);
        return new ResponseEntity<>("Movie was deleted by id: " + deletedMovie.getId(), HttpStatus.OK);
    }
}

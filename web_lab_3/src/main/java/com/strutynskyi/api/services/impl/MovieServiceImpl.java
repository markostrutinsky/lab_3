package com.strutynskyi.api.services.impl;

import com.strutynskyi.api.dto.movie.CreateMovieRequestDTO;
import com.strutynskyi.api.dto.movie.UpdateMovieRequestDTO;
import com.strutynskyi.api.exceptions.MovieAlreadyExistsException;
import com.strutynskyi.api.exceptions.NoMoviesByDirectorFoundException;
import com.strutynskyi.api.exceptions.NoSuchMovieException;
import com.strutynskyi.api.mappers.MovieMappers;
import com.strutynskyi.api.models.Director;
import com.strutynskyi.api.models.Movie;
import com.strutynskyi.api.repositories.DirectorRepository;
import com.strutynskyi.api.repositories.MovieRepository;
import com.strutynskyi.api.services.interfaces.DirectorService;
import com.strutynskyi.api.services.interfaces.MovieService;
import com.strutynskyi.api.validators.RequestDTOValidator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final DirectorService directorService;
    private final RequestDTOValidator<CreateMovieRequestDTO> createMovieRequestDTOValidator;
    private final RequestDTOValidator<UpdateMovieRequestDTO> updateMovieRequestDTOValidator;
    private static final Logger logger = LogManager.getLogger("project");

    @Override
    public List<Movie> findAll() {
        logger.info("MovieServiceImpl:findAll() Retrieving all movies from repository");
        List<Movie> movies = movieRepository.findAll();
        movies.sort(Comparator.comparing(Movie::getId));
        logger.info("MovieServiceImpl:findAll() Retrieved {} movies", movies.size());
        return movies;
    }

    @Override
    public Movie findById(Long id) {
        Movie foundMovie = movieRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("MovieServiceImpl:findById() Movie not found by id: {}", id);
                    throw new NoSuchMovieException();
                });
        logger.info("MovieServiceImpl:findById() Movie with id {} found in repository", id);
        return foundMovie;
    }

    @Override
    public List<Movie> findByDirector(String firstName, String lastName) {
        logger.info("MovieServiceImpl:findByDirector() Retrieving movies from repository by name {} {}", firstName, lastName);

        boolean directorExists = directorService.existsByFirstNameAndLastName(firstName, lastName);
        if (!directorExists) {
            logger.error("MovieServiceImpl:findByDirector() Director not found by name {} {}", firstName, lastName);
            throw new NoMoviesByDirectorFoundException(firstName, lastName);
        }
        List<Movie> movies = movieRepository.findByDirector(firstName, lastName).orElse(List.of());
        logger.info("MovieServiceImpl:findByDirector() Retrieved {} movies from repository by name {} {}", movies.size(),firstName, lastName);
        return movies;
    }

    @Override
    public Movie save(CreateMovieRequestDTO movieDTO) {
        createMovieRequestDTOValidator.validate(movieDTO);
        Director existingDirector = directorService.findById(movieDTO.getDirectorId());
        Movie movieToSave = MovieMappers.toMovieFromCreateDTO(movieDTO);
        movieToSave.setDirector(existingDirector);

        Optional<Movie> existingMovie = movieRepository.findAll()
                .stream()
                .filter(m -> m.equals(movieToSave))
                .findFirst();

        if (existingMovie.isPresent()) {
            logger.error("MovieServiceImpl:save() Movie already exists with id: {}", existingMovie.get().getId());
            throw new MovieAlreadyExistsException();
        }
        Movie savedMovie = movieRepository.save(movieToSave);
        logger.info("MovieServiceImpl:save() Saved new movie with id: {}", savedMovie.getId());
        return savedMovie;
    }

    @Override
    public Movie update(Long id, UpdateMovieRequestDTO movieDTO) {
        updateMovieRequestDTOValidator.validate(movieDTO);
        Movie validMovieModel = MovieMappers.toMovieFromUpdateDTO(movieDTO);
        Movie movieToUpdate = movieRepository.findById(id)
                .orElseThrow(
                        () -> {
                            logger.error("MovieServiceImpl:update() Movie not found with id: {}", id);
                            throw new NoSuchMovieException();
                        });

        movieToUpdate.setTitle(validMovieModel.getTitle());
        movieToUpdate.setGenre(validMovieModel.getGenre());
        movieToUpdate.setReleaseDate(validMovieModel.getReleaseDate());
        movieToUpdate.setDuration(validMovieModel.getDuration());

        Movie savedMovie = movieRepository.save(movieToUpdate);
        logger.info("MovieServiceImpl:update() Updated movie with id: {}", savedMovie.getId());

        return savedMovie;
    }

    @Override
    public Movie delete(Long id) {
        Movie movieToDelete = movieRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("MovieServiceImpl:delete() Movie not found with id: {}", id);
                    throw new NoSuchMovieException();
                });

        movieRepository.deleteById(id);
        logger.info("MovieServiceImpl:delete() Deleted movie by id: {}", movieToDelete.getId());

        return movieToDelete;
    }
}

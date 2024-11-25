package com.strutynskyi.api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genre;
    private LocalDate releaseDate;
    private Duration duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    public Movie(String title, String genre, LocalDate releaseDate, Duration duration) {
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(title.toLowerCase(), movie.title.toLowerCase())
                && Objects.equals(genre.toLowerCase(), movie.genre.toLowerCase())
                && Objects.equals(releaseDate, movie.releaseDate)
                && Objects.equals(duration, movie.duration)
                && Objects.equals(getDirector().getId(), movie.getDirector().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genre, releaseDate, duration, getDirector().getId());
    }
}

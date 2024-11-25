package com.strutynskyi.api.dto.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDate;

@Data
@ToString
public class UpdateMovieRequestDTO {
    @NotBlank(message = "title: was filled incorrectly. It cannot consist only of spaces or be left unfilled.")
    private String title;


    @NotBlank(message = "genre: was filled incorrectly. It cannot consist only of spaces or be left unfilled.")
    private String genre;


    @NotNull(message = "date: cannot be left unfilled.")
    @Past(message = "date: must be in the past.")
    private LocalDate releaseDate;


    @NotNull(message = "duration: cannot be left unfilled.")
    private Duration duration;


    @JsonCreator
    public UpdateMovieRequestDTO(
            @JsonProperty("title") String title,
            @JsonProperty("genre") String genre,
            @JsonProperty("releaseDate") LocalDate releaseDate,
            @JsonProperty("duration") Duration duration
    ) {
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}

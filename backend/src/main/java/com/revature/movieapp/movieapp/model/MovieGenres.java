package com.revature.movieapp.movieapp.model;
import java.io.Serializable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "MovieGenres")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieGenres {

    @EmbeddedId
    private MovieGenresId id;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @MapsId("genreId")
    @JoinColumn(name = "genre_id")
    private Genre genre;

    // Composite key class
    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MovieGenresId implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name = "movie_id")
        private Long movieId;

        @Column(name = "genre_id")
        private Long genreId;
    }
}
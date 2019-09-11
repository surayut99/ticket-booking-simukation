package theatre.movies;

import java.time.LocalDate;

public class ComingSoonMovies extends Movies{
    private LocalDate comingSoonDate;

    public ComingSoonMovies(String title, String length, LocalDate come_inDate, String locationPoster) {
        super(title, length, locationPoster);
        this.comingSoonDate = come_inDate;
    }

    public LocalDate getComingSoonDate() {
        return comingSoonDate;
    }
}

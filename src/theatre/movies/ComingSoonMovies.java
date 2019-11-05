package theatre.movies;

import java.time.LocalDate;

public class ComingSoonMovies extends Movies{
    private LocalDate comingSoonDate;

    public ComingSoonMovies(String title, String length, LocalDate come_inDate, String locationPoster, String description, String vdoPath) {
        super(title, length, locationPoster, description, vdoPath);
        this.comingSoonDate = come_inDate;
    }

    public LocalDate getComingSoonDate() {
        return comingSoonDate;
    }
}

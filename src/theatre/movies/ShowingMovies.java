package theatre.movies;

import java.time.LocalDate;

public class ShowingMovies extends Movies{
    private LocalDate come_inDate;

    public ShowingMovies(String title, String length, LocalDate come_inDate, String locationPoster, String description, String vdoPath) {
        super(title, length, locationPoster, description, vdoPath);
        this.come_inDate =come_inDate;
    }

    public LocalDate getCome_inDate() {
        return come_inDate;
    }
}

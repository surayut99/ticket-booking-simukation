package theatre;

import java.time.LocalDate;

public class ShowingMovies extends Movies{
    private LocalDate come_inDate;

    public ShowingMovies(String title, String length, LocalDate come_inDate, String locationPoster) {
        super(title, length, locationPoster);
        this.come_inDate =come_inDate;
    }
}

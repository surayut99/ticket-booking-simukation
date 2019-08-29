package theatre;

import java.time.LocalDate;

public class Movies extends Theatre {
    private String title;
    private String length;
    private String locationPoster;

    public Movies(String title, String length, String locationPoster) {
        this.title = title;
        this.length = length;
        this.locationPoster = locationPoster;
    }

    public String getTitle() {
        return title;
    }

    public String getLength() {
        return length;
    }

    public String getLocationPoster() {
        return locationPoster;
    }
}

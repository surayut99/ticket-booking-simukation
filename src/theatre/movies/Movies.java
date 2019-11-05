package theatre.movies;

public class Movies{
    private String title;
    private String length;
    private String posterLocation;
    private String description;
    private String vdoPath;

    public Movies(String title, String length, String posterLocation, String description, String vdoPath) {
        this.title = title;
        this.length = length;
        this.posterLocation = posterLocation;
        this.description = description;
        this.vdoPath = vdoPath;
    }

    public String getVdoPath() {
        return vdoPath;
    }

    public String getTitle() {
        return title;
    }

    public String getLength() {
        return length;
    }

    public String getPosterLocation() {
        return posterLocation;
    }

    public String getDescription() {
        return description;
    }
}

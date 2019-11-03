package theatre.movies;

public class Movies{
    private String title;
    private String length;
    private String posterLocation;

    public Movies(String title, String length, String posterLocation) {
        this.title = title;
        this.length = length;
        this.posterLocation = posterLocation;
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
}

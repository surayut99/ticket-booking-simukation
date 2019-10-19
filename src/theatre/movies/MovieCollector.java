package theatre.movies;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import theatre.NodeCreator;

import java.util.ArrayList;

public class MovieCollector {

    public static ArrayList<Movies> moviesList = new ArrayList<>();

    public static ArrayList<AnchorPane> generateMockUp(Class classname) {
        ArrayList<AnchorPane> mockUpList = new ArrayList<>();

        for (Movies movies : moviesList) {
            if (movies.getClass() == classname) {
                ImageView poster = NodeCreator.createImageView(200, 280, movies.getLocationPoster(), false);
                Label title = NodeCreator.createLabel(movies.getTitle(), 18, "#ffffff");
                NodeCreator.setAlignmentNodeOnAnchorPane(title, 290. ,0.,0.,0.);
                NodeCreator.setAlignmentNodeOnAnchorPane(poster, 0., 0.,0., null);
                AnchorPane blockMockUp = NodeCreator.createAnchorPane(200, 320, poster, title);
                blockMockUp.setCursor(Cursor.HAND);
                mockUpList.add(blockMockUp);
            }
        }

        return mockUpList;
    }

    public static Movies findMovie(String title) {
        for (Movies movies : moviesList) {
            String moviesTitle = movies.getTitle().toLowerCase();
            String finder = title.toLowerCase();
            if (moviesTitle.equals(finder) || moviesTitle.contains(finder)) return movies;
        }

        return null;
    }
}

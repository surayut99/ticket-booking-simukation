package theatre.collection;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import theatre.NodeCreator;
import theatre.movies.MovieCollector;
import theatre.movies.ShowingMovies;

import java.time.LocalDate;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        MovieCollector.moviesList.add(new ShowingMovies(
                "Spider-Man: Homeless", "02:02:02", LocalDate.now(), "picture/poster/spider-man-homeless.jpg"
        ));
//        ArrayList<AnchorPane> anchorPanes = MovieCollector.generateMockUp(ShowingMovies.class);

//        System.out.println(anchorPanes.size());
        ImageView imageView = new ImageView("picture/poster/spider-man-homeless.jpg");
    }
}

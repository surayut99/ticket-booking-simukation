package theatre;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MovieDetail {
    private Movies selectedMovie;

    @FXML Label title, date, movieStatus;
    @FXML ImageView poster;
    @FXML AnchorPane seatPart, mainShowDetail;

    @FXML public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                title.setText(selectedMovie.getTitle());
                poster.setImage(new Image(selectedMovie.getLocationPoster()));

                if (selectedMovie.getClass() == ShowingMovies.class) {
                    ShowingMovies movie = (ShowingMovies) selectedMovie;
                    date.setText(movie.getCome_inDate().toString());
                    movieStatus.setText("(Showing)");
                }

                else {
                    ComingSoonMovies movies = (ComingSoonMovies) selectedMovie;
                    date.setText(movies.getComingSoonDate().toString());
                    movieStatus.setText("(Coming-soon)");
                }
            }
        });
    }

    public void setSelectedMovie(Movies selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    public void setSceneSize(ReadOnlyDoubleProperty sceneSize) {
        this.mainShowDetail.prefWidthProperty().bind(sceneSize);
    }
}

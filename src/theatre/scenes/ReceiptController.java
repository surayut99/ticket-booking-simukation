package theatre.scenes;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import theatre.movies.ShowingMovies;
import theatre.showingSystem.ShowingSystem;
import theatre.showingSystem.ShowingSystemCollector;
import theatre.tools.AccountCollector;
import theatre.tools.AccountData.Account;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReceiptController {
    private ShowingMovies movies;
    private ShowingSystem showingSystem;
    private int no_theatre;
    private double cost;
    private String seats;
    private String startTime;

    @FXML
    AnchorPane mainPane;

    @FXML
    ImageView poster;

    @FXML
    VBox userBox, movieBox, theatreBox;

    public void setMovie(ShowingMovies movies) {
        this.movies = movies;
    }

    public void setTheatre(int no_theatre) {
        showingSystem = ShowingSystemCollector.getShowingSystems()[no_theatre - 1];
        this.no_theatre = no_theatre;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setSeat(ArrayList<String> seats) {
        this.seats = String.join(" ", seats);
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Account account = AccountCollector.getCurrentAccount();
                String[] dataAcc = {account.getUsername(), account.getFirstName(), account.getLastName(), account.getMail()};
                String[] dataMovie = {movies.getTitle(), movies.getLength(), movies.getCome_inDate().toString()};
                String[] dataTheatre = {Integer.toString(no_theatre), showingSystem.getSystemType(), startTime, seats, "", cost + "0 THB"};
                poster.setImage(new Image(movies.getPosterLocation()));
                for (int i = 0; i < dataAcc.length; i++) {
                    Text t = ((Text) userBox.getChildren().get(i));
                    t.setText(t.getText() + dataAcc[i]);
                }

                for (int i = 0; i < dataTheatre.length; i++) {
                    Text t = ((Text) theatreBox.getChildren().get(i));
                    t.setText(t.getText() + dataTheatre[i]);
                }

                for (int i = 0; i < dataMovie.length; i++) {
                    Text t = ((Text) movieBox.getChildren().get(i));
                    t.setText(t.getText() + dataMovie[i]);
                }
                screenShot();
            }
        });
    }

    private void screenShot() {
        WritableImage writableImage = mainPane.snapshot(new SnapshotParameters(), null);
        File file = new File("Receipt.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

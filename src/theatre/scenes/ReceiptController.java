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
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReceiptController {
    private ShowingMovies movies;
    private ShowingSystem showingSystem;
    private int no_theatre;
    private double cost;
    private String seats;
    private String startTime;

    @FXML
    Text dateTime;

    @FXML
    AnchorPane mainPane;

    @FXML
    ImageView poster;

    @FXML
    VBox userBox, movieBox, theatreBox;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                String time = String.format("%02d:%02d", now.getHour(), now.getMinute());
                String date = String.format("%02d-%02d-%d", now.getDayOfMonth(), now.getMonthValue(), now.getYear());
                dateTime.setText(time + " " + date);

                Account account = AccountCollector.getCurrentAccount();
                String[] dataAcc = {account.getUsername(), account.getFirstName(), account.getLastName(), account.getMail()};
                String[] dataMovie = {movies.getTitle(), movies.getLength(), movies.getCome_inDate().toString()};
                String[] dataTheatre = {Integer.toString(no_theatre), showingSystem.getSystemType(), startTime, seats, "", cost + "0 THB"};
                poster.setImage(new Image(new File(movies.getPosterLocation()).toURI().toString()));
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

                time = time.replace(':', '-');
                time = String.format("%s-%02d", time, now.getSecond());
                String filename= account.getUsername() + "_" + time + "_" + date + ".png";
                screenShot(filename);
            }
        });
    }

    private void screenShot(String dateTime) {
        String pathFile = "data/receiptPic/" + dateTime;
        WritableImage writableImage = mainPane.snapshot(new SnapshotParameters(), null);
        File file = new File(pathFile);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}

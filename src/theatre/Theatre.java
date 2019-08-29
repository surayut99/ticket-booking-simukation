package theatre;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.swing.text.html.ImageView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Theatre {
    private Label preLabel;
    private EffectedPointer effectedPointer;
    private ObservableList<Label> labelList = FXCollections.observableArrayList();
    private ImageView poster;

    private ObservableList<Movies>collectShowingMovies = FXCollections.observableArrayList();
    private ObservableList<Movies>collectComingSoonMovies = FXCollections.observableArrayList();
    private ObservableList<AnchorPane>showMoviesInRow = FXCollections.observableArrayList();


    @FXML Label showingOption, comingSoonOption, accountOption, logInOption;
    @FXML VBox vBoxshow;

    @FXML public void initialize() {
        labelList.addAll(showingOption, comingSoonOption, accountOption, logInOption);
        preLabel = showingOption;
        effectedPointer = new EffectedPointer();

        addShowingMovie();
        addComingSoonMovie();
    }

    @FXML public void mouseEnterOnLabel(MouseEvent event) {
        for (int i = 0; i < labelList.size(); i++) {
            if (labelList.get(i).isHover() && !labelList.get(i).getStyle().equals("-fx-background-color: #1F618D;")) {
                effectedPointer.createChangingTimeline(labelList.get(i), 0);
            }
        }
    }

    @FXML public void mouseExitOnLabel(MouseEvent event) {
        for (int i = 0; i < labelList.size(); i++) {
            if (!labelList.get(i).isHover()) {
                effectedPointer.createChangingTimeline(labelList.get(i), 2);
            }
        }
    }

    @FXML public void mouseClickOnLabel(MouseEvent event) {
        for (int i = 0; i < labelList.size(); i++) {
            if (labelList.get(i).isHover() && labelList.get(i) != preLabel) {
                preLabel.setStyle("-fx-background-color: null");
                labelList.get(i).setEffect(effectedPointer.clickAndchangLabelColor());
                labelList.get(i).setStyle("-fx-background-color: #1F618D;");
                preLabel = labelList.get(i);
                break;
            }
        }
    }

    public void addShowingMovie() {
        LocalDate date = LocalDate.now();
        String comingDate = "09/09/2019";


        collectShowingMovies.add(new ShowingMovies("Spider-Man: Homeless", "02:02:02", date, "picture/poster/spider-man-homeless.jpg"));
        collectShowingMovies.add(new ShowingMovies("Boosty and the Beast","01:56:49", date, "picture/poster/booty_and_the_beast.jpg"));

    }

    public void addComingSoonMovie() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("09/09/2019", dateTimeFormatter);

        collectComingSoonMovies.add(new ComingSoonMovies("INCEPTION", "01:55:55", date, "picture/poster/inception.jpg"));
    }

//    public void showMovies() {
//        int countPosterOnRow = 0;
//
//        for (Movies movies : collectShowingMovies) {
//
//        }
//    }
}
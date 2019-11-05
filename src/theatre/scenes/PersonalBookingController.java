package theatre.scenes;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import theatre.seat.SeatController;
import theatre.showingSystem.Schedule;
import theatre.tools.AccountCollector;
import theatre.tools.AccountData.Account;
import theatre.tools.DataController;
import theatre.tools.NodeCreator;

import java.util.ArrayList;
import java.util.List;

public class PersonalBookingController {
    private Schedule selectedSchedule;
    private List<VBox> seatBox;
    private List<AnchorPane> selectedSeat;
    private int no_theatre;

    @FXML
    VBox showSeat;
    @FXML
    AnchorPane thisPage;
    @FXML
    Button editBtn, saveBtn;


    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                selectedSeat = new ArrayList<>();
                showSeat.getChildren().setAll(seatBox);
                Text text = new Text("Total: " + selectedSchedule.getTotalSeat() + " Available: "+ selectedSchedule.getNumAvailableSeat());
                text.setFont(Font.font("System", FontWeight.BOLD, 18));
                text.setFill(Color.WHITE);
                showSeat.getChildren().add(text);
                showSeat.setDisable(true);
                showStatusSeat();
            }
        });
    }

    @FXML
    public void actionOnClose(ActionEvent event) {
        AnchorPane parent = (AnchorPane) thisPage.getParent();
        parent.getChildren().remove(thisPage);
    }

    @FXML
    public void actionOnEdit(ActionEvent event) {
        showSeat.setDisable(false);
        saveBtn.setDisable(false);
        editBtn.setDisable(true);
    }

    @FXML
    public void actionOnSave(ActionEvent event) {
        showSeat.setDisable(true);
        saveBtn.setDisable(true);
        editBtn.setDisable(false);

        if (selectedSeat.isEmpty()) return;

        List<String> seatPosition = new ArrayList<>();
        for (AnchorPane a : selectedSeat) {
            a.setDisable(true);
            Label l = (Label) a.getChildren().get(1);
            seatPosition.add(l.getText());
        }


        Account account = AccountCollector.getCurrentAccount();
        List<String> userSeatPosition = account.getUserSeatPositions(no_theatre, selectedSchedule.getMovies(), selectedSchedule);
        String oldData = String.join(" ", userSeatPosition);
        userSeatPosition.removeAll(seatPosition);
        String newData = String.join(" ", userSeatPosition);
        String[] arrPosition = seatPosition.toArray(new String[seatPosition.size()]);
        selectedSchedule.delReservedSeat(arrPosition);
        account.removeBooking(no_theatre, selectedSchedule.getMovies().getTitle(), selectedSchedule, arrPosition);
        DataController.removeReservingData(Integer.toString(no_theatre + 1), oldData, newData, selectedSchedule.getStartTime(), selectedSchedule.getMovies().getTitle());
    }

    @FXML
    private void mouseClickOnSeat(MouseEvent event) {
        AnchorPane seat = (AnchorPane) event.getSource();
        if (seat.getChildren().size() == 3) {
            seat.getChildren().remove(seat.getChildren().size() - 1);
            selectedSeat.add(seat);
        }
        else {
            ImageView check = NodeCreator.createImageView(30, 30, "/picture/check.png", true);
            check.setEffect(new ColorAdjust(0,1,0.5,0));
            AnchorPane.setLeftAnchor(check, 17.5);
            AnchorPane.setBottomAnchor(check, 30.);
            seat.getChildren().add(check);
            selectedSeat.remove(seat);
        }
    }


    private void showStatusSeat() {
        Account account = AccountCollector.getCurrentAccount();
        List<String> userSeatPosition = account.getUserSeatPositions(no_theatre, selectedSchedule.getMovies(), selectedSchedule);
        for (String p : userSeatPosition) {
            int row = p.charAt(0) - 65;
            int column = Integer.parseInt(p.substring(1)) - 1;
            AnchorPane userSeat = SeatController.findPosition(showSeat, row, column, no_theatre);
            ImageView check = NodeCreator.createImageView(30, 30, "/picture/check.png", true);
            check.setEffect(new ColorAdjust(0,1,0.5,0));
            AnchorPane.setLeftAnchor(check, 17.5);
            AnchorPane.setBottomAnchor(check, 30.);
            userSeat.setOnMouseClicked(this::mouseClickOnSeat);
            userSeat.getChildren().add(check);
            userSeat.setDisable(false);
        }
    }

    public void setSelectedSchedule(Schedule selectedSchedule) {
        this.selectedSchedule = selectedSchedule;
    }

    public void setSeatBox(List<VBox> seatBox) {
        this.seatBox = seatBox;
    }

    public void setNo_theatre(int no_theatre) {
        this.no_theatre = no_theatre;
    }
}

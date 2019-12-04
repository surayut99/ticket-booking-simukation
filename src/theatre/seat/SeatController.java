package theatre.seat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import theatre.showingSystem.ShowingSystemCollector;
import theatre.tools.NodeCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeatController{

    public static Seat[][] createSeatArray(String pattern, int row, int column, double startPrice) {
        Seat[][] seats = new Seat[row][column];

        for (int i = 0; i < row; i++) {
            char letter = (char) ('A' + i);
            for (int j = 0; j < column; j++) {
                String position = Character.toString(letter) + (j + 1);
                if (pattern.equals("Normal")) {
                    seats[i][j] = createSeatObject("Normal", position, startPrice);
                    continue;
                }

                if (pattern.equals("Hybrid")) {
                    if (i == 0) { seats[i][j] = createSeatObject("VIP", position, startPrice); }
                    else if (i == 1 || i == 2) { seats[i][j] = createSeatObject("Premium", position, startPrice); }
                    else seats[i][j] = createSeatObject("Normal", position, startPrice);
                }
            }
        }
        return seats;
    }

    private static Seat createSeatObject(String type, String position, double startPrice) {
        switch (type) {
            case "Premium":
                PremiumSeat premiumSeat = new PremiumSeat(type, startPrice, "/picture/seat/premium.png", position, false);
                premiumSeat.increasePrice();
                return premiumSeat;

            case "VIP":
                VIPSeat vipSeat = new VIPSeat(type, startPrice, "/picture/seat/vip.png", position, false);
                vipSeat.increasePrice();
                return vipSeat;

            default:
                return new Seat(type, startPrice, "/picture/seat/normal.png", position, false);
        }
    }

    public static List<VBox> generateSeat(int no_theatre, int imgSize, boolean isDisable, EventHandler<MouseEvent> e) {
        Seat[][] seats = ShowingSystemCollector.getShowingSystems()[no_theatre].getSeats();
        String currentSeatType = "";
        VBox typeBox = null;
        List<VBox> vBoxList = new ArrayList<>();

        for (int i = seats.length - 1; i >= 0; i--) {
            if (!currentSeatType.equals(seats[i][0].getSeatType())) {
                typeBox = new VBox();
                typeBox.setPadding(new Insets(20, 0, 20, 0));
                typeBox.setEffect(new DropShadow());
                currentSeatType = seats[i][0].getSeatType();
                vBoxList.add(typeBox);
            }

            HBox rowSeat =
                    no_theatre == 4?
                    SeatController.createCouplePattern(seats[i], imgSize, isDisable, e):
                    SeatController.createNormalPattern(seats[i], imgSize, isDisable, e);

            rowSeat.setAlignment(Pos.CENTER);
            typeBox.getChildren().add(rowSeat);
        }
        return vBoxList;
    }

    private static HBox createNormalPattern(Seat[] seats, double imgSize, boolean isDisable, EventHandler<MouseEvent> e) {
        HBox rowSeats = new HBox(5);
        rowSeats.setPrefHeight(imgSize);
        rowSeats.setAlignment(Pos.CENTER);
        for (int i = 0; i < seats.length; i++) {
            AnchorPane groupImg = NodeCreator.createSeatIcon(imgSize, seats[i].getSeatPosition(), seats[i].getSeatImgPath());
            groupImg.setOnMouseClicked(e);
            groupImg.setDisable(isDisable);
            rowSeats.getChildren().add(groupImg);
        }
        return rowSeats;
    }

    private static HBox createCouplePattern(Seat[] seats, double imgSize, boolean isDisable, EventHandler<MouseEvent> e) {
        HBox rowSeats = new HBox(25);
        HBox coupleBox = new HBox(5);
        coupleBox.setPrefHeight(imgSize);
        coupleBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < seats.length; i++) {
            AnchorPane groupImg = NodeCreator.createSeatIcon(imgSize, seats[i].getSeatPosition(), seats[i].getSeatImgPath());
            groupImg.setOnMouseClicked(e);
            groupImg.setDisable(isDisable);
            coupleBox.getChildren().add(groupImg);
            if (coupleBox.getChildren().size() == 2) {
                rowSeats.getChildren().add(coupleBox);
                coupleBox = new HBox(5);
                coupleBox.setPrefHeight(imgSize);
                coupleBox.setAlignment(Pos.CENTER);
            }
        }
        return rowSeats;
    }

    public static AnchorPane findPosition(VBox box, int row, int column, int no_theatre) {
        if (no_theatre == 4) {
            HBox rowBox = findRowPosition(row, box);
            rowBox = (HBox) rowBox.getChildren().get(column / 2);
            return (AnchorPane) rowBox.getChildren().get(column % 2);
        }
        return (AnchorPane) (findRowPosition(row, box).getChildren().get(column));
    }

    private static HBox findRowPosition(int row, VBox box) {
        ObservableList<Node> typeBoxes = FXCollections.observableArrayList(box.getChildren());
        FXCollections.reverse(typeBoxes);
        typeBoxes.remove(0);
        int maxRange = 0;

        for (Node node : typeBoxes) {
            VBox checking = (VBox) node;
            maxRange += checking.getChildren().size();
            if (row < maxRange) {
                ObservableList<Node> rowBoxes = FXCollections.observableArrayList(checking.getChildren());
                FXCollections.reverse(rowBoxes);
                int index = row - maxRange + checking.getChildren().size();
                return (HBox) rowBoxes.get(index);
            }
        }
        return null;
    }
}

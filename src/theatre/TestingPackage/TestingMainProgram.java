package theatre.TestingPackage;

import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.TestingPackage.TestingSeat.TestingPremiumSeat;
import theatre.TestingPackage.TestingSeat.TestingSeat;
import theatre.TestingPackage.TestingSeat.TestingVIPSeat;
import theatre.TestingPackage.TestingShowingSystem.TestingShowingSystem;

import java.time.LocalDate;

public class TestingMainProgram {
    public static void main(String[] args) {
        int row = 15, column = 8;
        TestingSeat[][] hybridSeats = new TestingSeat[15][10];
        TestingSeat[][] normalSeats = new TestingSeat[10][10];

        for (int i = 0; i < row; i++) {
            char letter = (char) ('A' + i);
            if (0 <= i && i <= 1) {hybridSeats[i] = generateSeat(column, letter, "VIP");}
            else if (2 <= i && i <= 4) {hybridSeats[i] = generateSeat(column, letter, "Premium");}
            else {hybridSeats[i] = generateSeat(column, letter, "Normal");}
        }

        for (int i = 0; i < 10; i++) {
            char letter = (char) ('A' + i);
            normalSeats[i] = generateSeat(10, letter, "Normal");
        }

        Movies spider_man = new ShowingMovies(
                "Spider-Man: Homeless", "02:02:02", LocalDate.now(), "picture/poster/spider-man-homeless.jpg"
        );

        TestingShowingSystem[] systems = new TestingShowingSystem[5];
        TestingShowingSystem normalSys_Seat = new TestingShowingSystem("Normal", 0, normalSeats);
        normalSys_Seat.addSequenceMovies(spider_man);
        normalSys_Seat.addSequenceMovies(spider_man);
        normalSys_Seat.addSequenceMovies(spider_man);
    }

    public static TestingSeat[] generateSeat(int column, char letter, String type) {
        TestingSeat[] seats = new TestingSeat[column];
        for (int i = 0; i < column; i++) {
            String position = Character.toString(letter) + (i + 1);
            switch (type) {
                case "Premium":
                    TestingPremiumSeat premiumSeat = new TestingPremiumSeat(type, 200, "picture/seat/premium.png", position, false);
                    premiumSeat.increasePrice();
                    seats[i] = premiumSeat;
                    break;

                case "VIP":
                    TestingVIPSeat vipSeat = new TestingVIPSeat(type, 200, "picture/seat/vip.png", position, false);
                    vipSeat.increasePrice();
                    seats[i] = vipSeat;
                    break;

                default:
                    seats[i] = new TestingSeat(type, 200, "picture/seat/normal.png", position, false);
                    break;
            }
        }

        return seats;
    }
}
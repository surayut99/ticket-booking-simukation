package theatre.tools.AccountData;

import theatre.movies.Movies;
import theatre.showingSystem.Schedule;
import theatre.tools.DataController;

import java.util.*;

public class Account extends Observable {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String mail;
    private String secureQuestion;
    private String answer;
    private double balance;
    private ArrayList<SelectedTheatre> selectedTheatres;

    public Account(String username, String password, String firstName, String lastName, String mail, String secureQuestion, String answer, double balance) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.secureQuestion = secureQuestion;
        this.answer = answer;
        this.balance = balance;
        selectedTheatres = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getBalance() {
        return balance;
    }

    public void addBooking(int no_theatre, String title, Schedule schedule, String[] position) {
        for (SelectedTheatre selectedTheatre : selectedTheatres) {
            if (selectedTheatre.getNo_theatre() == no_theatre) {
                selectedTheatre.addBookingData(title, schedule, position);
                return;
            }
        }
        SelectedTheatre selectedTheatre = new SelectedTheatre(no_theatre);
        selectedTheatre.addBookingData(title, schedule, position);
        selectedTheatres.add(selectedTheatre);
        Collections.sort(selectedTheatres, new Comparator<SelectedTheatre>() {
            @Override
            public int compare(SelectedTheatre o1, SelectedTheatre o2) {
                return Integer.compare(o1.getNo_theatre(), o2.getNo_theatre());
            }
        });
    }

    public void removeBooking(int no_theatre, String title, Schedule schedule, String[] position) {
        SelectedTheatre selectedTheatre = getSelectedTheatreByNumber(no_theatre);
        selectedTheatre.removeBookingData(title, schedule, position);
        if (selectedTheatre.getSelectedMoviesList().isEmpty()) {
            selectedTheatres.remove(selectedTheatre);
        }
    }

    public void topUp(int amount) {
        balance += amount;
        DataController.editBalance(username, balance);

        setChanged();
        notifyObservers();
    }

    public String getMail() {
        return mail;
    }

    public String getSecureQuestion() {
        return secureQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void purchase(double amount) {
        if (balance - amount < 0)
            throw new IllegalArgumentException("Your balance is not enough, please check and try again");
        balance -= amount;
        DataController.editBalance(username ,balance);
    }

    public ArrayList<SelectedTheatre> getSelectedTheatres() {
        return selectedTheatres;
    }

    public ArrayList<String> getUserSeatPositions(int theatre, Movies movies, Schedule schedule) {
        ArrayList<String> position;
        try {
            SelectedTheatre currentTheatre = getSelectedTheatreByNumber(theatre);
            SelectedMovies selectedMovies = getSelectedMovieByObject(currentTheatre, movies);
            int index = selectedMovies.getSchedules().indexOf(schedule);
            if (index == -1) throw new IllegalArgumentException();
            position = new ArrayList<>(Arrays.asList(selectedMovies.getBookedSeat().get(index).split(",")));
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
        return position;
    }

    public SelectedTheatre getSelectedTheatreByNumber(int theatre) {
        for (SelectedTheatre t : selectedTheatres) {
            if (t.getNo_theatre() == theatre) {
                return t;
            }
        }
        throw new IllegalArgumentException();
    }

    private SelectedMovies getSelectedMovieByObject(SelectedTheatre theatre, Movies movies) {
         for (SelectedMovies m : theatre.getSelectedMoviesList()) {
             if (m.getMovies() == movies) {
                 return m;
             }
         }
         throw new IllegalArgumentException();
    }
}

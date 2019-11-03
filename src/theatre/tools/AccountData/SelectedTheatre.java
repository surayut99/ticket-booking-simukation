package theatre.tools.AccountData;

import theatre.movies.MovieCollector;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.showingSystem.Schedule;
import theatre.showingSystem.ShowingSystem;
import theatre.showingSystem.ShowingSystemCollector;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class SelectedTheatre {
    private int no_theatre;
    private ArrayList<SelectedMovies> selectedMoviesList;

    public SelectedTheatre(int no_theatre) {
        this.no_theatre = no_theatre;
        this.selectedMoviesList = new ArrayList<>();
    }

    public void addBookingData(String titleMovie, Schedule schedule, String[] position) {
        SelectedMovies selectedMovies = getSelectedMovie(titleMovie);

        if (selectedMovies != null) {
            selectedMovies.addSchedule(schedule, position);
        }

        else {
            selectedMovies = new SelectedMovies(MovieCollector.findMovie(titleMovie));
            selectedMovies.addSchedule(schedule, position);
            selectedMoviesList.add(selectedMovies);
        }
    }

    public void removeBookingData(String titleMovie, Schedule schedule, String[] position) {
        SelectedMovies selectedMovies = getSelectedMovie(titleMovie);
        selectedMovies.removeSeat(schedule, position);
        if (selectedMovies.getSchedules().isEmpty()) {
            selectedMoviesList.remove(selectedMovies);
        }
    }

    private SelectedMovies getSelectedMovie(String titleMovie) {
        for (SelectedMovies selectedMovies : selectedMoviesList) {
            if (selectedMovies.getMovies().getTitle().equals(titleMovie)) {
                return  selectedMovies;
            }
        }

        return null;
    }

    public int getNo_theatre() {
        return no_theatre;
    }

    public ArrayList<SelectedMovies> getSelectedMoviesList() {
        return selectedMoviesList;
    }
}

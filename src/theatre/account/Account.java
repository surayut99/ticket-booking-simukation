package theatre.account;

import theatre.movies.Movies;

public class Account {
    private Movies currentSelectMovie;

    public void setCurrentSelectMovie(Movies currentSelectMovie) {
        this.currentSelectMovie = currentSelectMovie;
    }

    public Movies getCurrentSelectMovie() {
        return currentSelectMovie;
    }
}

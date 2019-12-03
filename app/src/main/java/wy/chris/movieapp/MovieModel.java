package wy.chris.movieapp;

public class MovieModel {

    public String imagelink;
    public String moviename;
    public String movierating;

    public MovieModel(String imagelink, String moviename, String movierating) {
        this.imagelink = imagelink;
        this.moviename = moviename;
        this.movierating = movierating;
    }

    public MovieModel() {
    }
}

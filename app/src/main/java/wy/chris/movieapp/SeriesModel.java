package wy.chris.movieapp;

public class SeriesModel {

    public String imagelink;
    public String seriesname;
    public String seriesrating;

    public SeriesModel(String imagelink, String seriesname, String seriesrating) {
        this.imagelink = imagelink;
        this.seriesname = seriesname;
        this.seriesrating = seriesrating;
    }

    public SeriesModel() {
    }
}

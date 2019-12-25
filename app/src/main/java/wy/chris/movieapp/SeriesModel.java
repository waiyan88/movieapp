package wy.chris.movieapp;

public class SeriesModel {
    public String seriesName;
    public String seriesImageLink;
    public String seriesVideoLink;
    public String seriesCategory;

    public SeriesModel(String seriesName, String seriesImageLink, String seriesVideoLink, String seriesCategory) {
        this.seriesName = seriesName;
        this.seriesImageLink = seriesImageLink;
        this.seriesVideoLink = seriesVideoLink;
        this.seriesCategory = seriesCategory;
    }

    public SeriesModel() {
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesImageLink() {
        return seriesImageLink;
    }

    public void setSeriesImageLink(String seriesImageLink) {
        this.seriesImageLink = seriesImageLink;
    }

    public String getSeriesVideoLink() {
        return seriesVideoLink;
    }

    public void setSeriesVideoLink(String seriesVideoLink) {
        this.seriesVideoLink = seriesVideoLink;
    }

    public String getSeriesCategory() {
        return seriesCategory;
    }

    public void setSeriesCategory(String seriesCategory) {
        this.seriesCategory = seriesCategory;
    }
}

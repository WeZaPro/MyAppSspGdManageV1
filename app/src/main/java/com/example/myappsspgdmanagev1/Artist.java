package com.example.myappsspgdmanagev1;


// Model
public class Artist {
    private String artistName;
    private String artistJob;
    private String artistGenre;
    private String imgUrl;

    public Artist() {
        //this constructor is required
    }


    public Artist(String artistName,String artisJob, String artistGenre, String imgUrl) {

        this.artistName = artistName;
        this.artistJob = artisJob;
        this.artistGenre = artistGenre;
        this.imgUrl = imgUrl;
    }


    public String getArtistName() {
        return artistName;
    }

    public String getArtistGenre() {
        return artistGenre;
    }

    public String getArtistJob() {
        return artistJob;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
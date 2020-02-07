package com.example.myappsspgdmanagev1;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

// Model
public class Artist implements Parcelable {
    private String artistName;
    private String artistJob;
    private String artistGenre;
    private String imgUrl;
    private String idname;

    public Artist() {
        //this constructor is required
    }

    public Artist(String artistName,String idname) {
        this.artistName = artistName;
        this.idname = idname;
    }

    public Artist(String artistName, String artisJob, String artistGenre, String imgUrl) {

        this.artistName = artistName;
        this.artistJob = artisJob;
        this.artistGenre = artistGenre;
        this.imgUrl = imgUrl;
    }


    protected Artist(Parcel in) {
        artistName = in.readString();
        artistJob = in.readString();
        artistGenre = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(artistName);
        parcel.writeString(artistJob);
        parcel.writeString(artistGenre);
        parcel.writeString(imgUrl);
    }
}
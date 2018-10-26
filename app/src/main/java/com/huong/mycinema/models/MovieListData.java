package com.huong.mycinema.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huong.mycinema.ui.signup.User;

import java.io.Serializable;

/**
 * Created by HuongPN on 10/17/2018.
 */
public class MovieListData implements Serializable, Parcelable{
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("creatorId")
    @Expose
    private String creatorId;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("posterURL")
    @Expose
    private String posterURL;
    @SerializedName("releaseDate")
    @Expose
    private long releaseDate;
    @SerializedName("user")
    @Expose
    private User user;

    public MovieListData(String id, String content, String name, String creatorId, String genre, String posterURL, long releaseDate, User user) {
        this.id = id;
        this.content = content;
        this.name = name;
        this.creatorId = creatorId;
        this.genre = genre;
        this.posterURL = posterURL;
        this.releaseDate = releaseDate;
        this.user = user;
    }

    protected MovieListData(Parcel in) {
        id = in.readString();
        content = in.readString();
        name = in.readString();
        creatorId = in.readString();
        genre = in.readString();
        posterURL = in.readString();
        releaseDate = in.readLong();
    }

    public static final Creator<MovieListData> CREATOR = new Creator<MovieListData>() {
        @Override
        public MovieListData createFromParcel(Parcel in) {
            return new MovieListData(in);
        }

        @Override
        public MovieListData[] newArray(int size) {
            return new MovieListData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(content);
        parcel.writeString(name);
        parcel.writeString(creatorId);
        parcel.writeString(genre);
        parcel.writeString(posterURL);
        parcel.writeLong(releaseDate);
    }
}

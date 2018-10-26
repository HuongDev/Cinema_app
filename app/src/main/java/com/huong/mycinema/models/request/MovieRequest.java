package com.huong.mycinema.models.request;

import com.google.gson.JsonObject;

import java.io.File;

/**
 * Created by HuongPN on 10/17/2018.
 */
public class MovieRequest {
    private String name;
    private String genre;
    private long releaseDate;
    private String content;
    private String creatorId;
    private File file;

    public MovieRequest(String name, String genre, long releaseDate, String content, String creatorId, File file) {
        this.name = name;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.content = content;
        this.creatorId = creatorId;
        this.file = file;
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

    public long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public File getFile() {
        return file;
    }

    public JsonObject toJsonObject() {
        JsonObject body = new JsonObject();
        body.addProperty("name", getName());
        body.addProperty("genre", getGenre());
        body.addProperty("releaseDate", getReleaseDate());
        body.addProperty("content", getContent());
        body.addProperty("creatorId", getCreatorId());
        return body;
    }
}

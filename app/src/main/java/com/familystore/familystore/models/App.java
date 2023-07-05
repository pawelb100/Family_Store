package com.familystore.familystore.models;

import java.util.ArrayList;
import java.util.List;

public class App extends AppPreview {


    private String authorId;

    private long lastUpdated = -1;

    private String description = "";

    private String changelog = "";

    private List<String> pictureUrls = new ArrayList<>();

    private String downloadUrl = "";

    public App() {
        super();
    }

    public App(String id, String name, String pictureUrl, String version, String author, long lastUpdated, String description, String changelog, List<String> pictureUrls, String downloadUrl) {
        super(id, name, pictureUrl, version);
        this.authorId = author;
        this.lastUpdated = lastUpdated;
        this.description = description;
        this.changelog = changelog;
        this.pictureUrls = pictureUrls;
        this.downloadUrl = downloadUrl;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public List<String> getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(List<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}



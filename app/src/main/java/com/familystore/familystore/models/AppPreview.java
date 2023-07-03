package com.familystore.familystore.models;

public class AppPreview {

    private String id;
    private String name;

    private String logoUrl;

    private String version;

    public AppPreview(String id, String name, String pictureUrl, String version) {
        this.id = id;
        this.name = name;
        this.logoUrl = pictureUrl;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

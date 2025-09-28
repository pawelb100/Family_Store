package com.familystore.familystore.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class App extends AppPreview {
    @SerializedName("description")
    private String description;

    @SerializedName("changelog")
    private String changelog;

    private String downloadUrl;
    private List<String> pictureUrls = new ArrayList<>();
}



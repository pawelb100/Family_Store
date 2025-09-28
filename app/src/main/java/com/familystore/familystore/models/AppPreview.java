package com.familystore.familystore.models;

import com.google.gson.annotations.SerializedName;

import java.time.OffsetDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AppPreview {
    @EqualsAndHashCode.Include
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("version")
    private String version;

    @SerializedName("brand")
    private Brand brand;

    @SerializedName("created_at")
    private OffsetDateTime createdAt;

    @SerializedName("last_updated")
    private OffsetDateTime lastUpdated = null;

    private String logoUrl = "";
}

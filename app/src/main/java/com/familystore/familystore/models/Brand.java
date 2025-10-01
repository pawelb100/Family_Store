package com.familystore.familystore.models;


import com.google.gson.annotations.SerializedName;

public record Brand(
        @SerializedName("id")
        int id,

        @SerializedName("name")
        String name
) {
}

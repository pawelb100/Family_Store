package com.familystore.familystore.models;

import com.google.gson.annotations.SerializedName;

public record FSReleaseData(
        @SerializedName("release_id") String releaseId,
        @SerializedName("download_url") String downloadUrl
) {
}

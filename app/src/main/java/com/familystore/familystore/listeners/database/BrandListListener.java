package com.familystore.familystore.listeners.database;

import com.familystore.familystore.models.Brand;

import java.util.List;

public interface BrandListListener {
    void onResult(List<Brand> result);
}

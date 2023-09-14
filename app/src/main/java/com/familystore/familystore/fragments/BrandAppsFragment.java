package com.familystore.familystore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.familystore.familystore.R;
import com.familystore.familystore.adapters.AppPreviewListAdapter;
import com.familystore.familystore.databinding.FragmentBrandAppsBinding;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.models.Brand;
import com.familystore.familystore.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class BrandAppsFragment extends Fragment {

    private FragmentBrandAppsBinding binding;
    private AppPreviewListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBrandAppsBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        MainViewModel viewModel = viewModelProvider.get(MainViewModel.class);

        adapter = null;

        assert getArguments() != null;
        String id = getArguments().getString("brandId");
        String brandName = getArguments().getString("brandName");
        binding.tvBrand.setText(getString(R.string.brand_title, brandName));

        viewModel.addAppPreviewListListener(result -> setAdapter(result, new Brand(id, brandName)));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setAdapter(List<AppPreview> apps, Brand brand) {

        if (binding != null) {

            List<Brand> brands = List.of(brand);

            adapter = new AppPreviewListAdapter(getContext(), apps, brands, id -> {
                Bundle bundle = new Bundle();
                bundle.putString("appId", id);
                bundle.putBoolean("fromBrand", true);

                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_brandAppsFragment_to_appFragment, bundle);
            });
            adapter.filterByBrandId(brand.getId());
            adapter.sort(AppPreview.Order.PUBLISHED);

            binding.rvAppList.setAdapter(adapter);
            binding.rvAppList.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}
package com.familystore.familystore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.familystore.familystore.R;
import com.familystore.familystore.adapters.AppPreviewListAdapter;
import com.familystore.familystore.databinding.FragmentBrandAppsBinding;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.models.AppSortOrder;
import com.familystore.familystore.viewmodels.MainViewModel;

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
        int id = getArguments().getInt("brandId");
        String brandName = getArguments().getString("brandName");
        binding.tvBrand.setText(getString(R.string.brand_title, brandName));

        viewModel.getAppPreviewList(
                result -> setAdapter(result, id),
                this::onFetchError
        );
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setAdapter(List<AppPreview> apps, int brandId) {

        if (binding != null) {
            adapter = new AppPreviewListAdapter(getContext(), apps, id -> {
                Bundle bundle = new Bundle();
                bundle.putInt("appId", id);
                bundle.putBoolean("fromBrand", true);

                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_brandAppsFragment_to_appFragment, bundle);
            });
            adapter.filterByBrandId(brandId);
            adapter.sort(AppSortOrder.PUBLISHED);

            binding.rvAppList.setAdapter(adapter);
            binding.rvAppList.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private void onFetchError() {
        Toast.makeText(
                getContext(),
                getString(R.string.app_list_fetch_error),
                Toast.LENGTH_SHORT
        ).show();
    }
}
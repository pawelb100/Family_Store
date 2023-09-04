package com.familystore.familystore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.familystore.familystore.R;
import com.familystore.familystore.adapters.AppPreviewListAdapter;
import com.familystore.familystore.databinding.FragmentBrandAppsBinding;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.viewmodels.MainViewModel;

import java.util.List;

public class BrandAppsFragment extends Fragment {

    private FragmentBrandAppsBinding binding;
    private AppPreviewListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBrandAppsBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        MainViewModel viewModel = viewModelProvider.get(MainViewModel.class);

        adapter = null;

        assert getArguments() != null;
        String id = getArguments().getString("brandId");
        String brandName = getArguments().getString("brandName");
        binding.tvBrand.setText(getString(R.string.brand_title, brandName));

        viewModel.addAppPreviewListListener(result -> setAdapter(result, id));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setAdapter(List<AppPreview> apps, String brandId) {

        if (binding != null) {
            adapter = new AppPreviewListAdapter(getContext(), apps, id -> {
                Bundle bundle = new Bundle();
                bundle.putString("appId", id);
                bundle.putBoolean("fromBrand", true);

                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_brandAppsFragment_to_appFragment, bundle);
            });
            adapter.filterByBrandId(brandId);
            adapter.sort(AppPreview.Order.PUBLISHED);

            binding.rvAppList.setAdapter(adapter);
            binding.rvAppList.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}
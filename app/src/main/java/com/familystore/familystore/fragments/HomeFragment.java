package com.familystore.familystore.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.familystore.familystore.R;
import com.familystore.familystore.adapters.AppPreviewListAdapter;
import com.familystore.familystore.databinding.FragmentHomeBinding;
import com.familystore.familystore.listeners.database.AppPreviewListListener;
import com.familystore.familystore.listeners.database.BrandListListener;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.models.Brand;
import com.familystore.familystore.utils.SettingsManager;
import com.familystore.familystore.viewmodels.MainViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private AppPreviewListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        MainViewModel viewModel = viewModelProvider.get(MainViewModel.class);

        adapter = null;

        viewModel.getBrandList(brands ->
                viewModel.addAppPreviewListListener(apps ->
                        setAdapter(apps, brands)
                )
        );

        binding.btnSortPublished.setOnClickListener(view -> adapter.sort(AppPreview.Order.PUBLISHED));
        binding.btnSortLastUpdated.setOnClickListener(view -> adapter.sort(AppPreview.Order.LAST_UPDATED));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setAdapter(List<AppPreview> apps, List<Brand> brands) {

        if (binding != null) {
            adapter = new AppPreviewListAdapter(getContext(), apps, brands, id -> {
                Bundle bundle = new Bundle();
                bundle.putString("appId", id);

                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_homeFragment_to_appFragment, bundle);
            });

            binding.rvAppList.setAdapter(adapter);
            binding.rvAppList.setLayoutManager(new LinearLayoutManager(getContext()));

            // default sorting
            Context ctx = getContext();
            assert ctx != null;
            SettingsManager settingsManager = new SettingsManager(
                    PreferenceManager.getDefaultSharedPreferences(ctx),
                    getContext()
            );
            AppPreview.Order defaultSorting = settingsManager.getDefaultAppSorting();
            switch (defaultSorting) {
                case PUBLISHED:
                    binding.btnSortPublished.performClick();
                    break;
                case LAST_UPDATED:
                    binding.btnSortLastUpdated.performClick();
                    break;
            }
            adapter.sort(defaultSorting);
        }
    }


}
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
import com.familystore.familystore.adapters.AppListAdapter;
import com.familystore.familystore.databinding.FragmentHomeBinding;
import com.familystore.familystore.models.AppPreview;
import com.familystore.familystore.viewmodels.MainViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private MainViewModel viewModel;

    private AppListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(MainViewModel.class);

        adapter = null;

        viewModel.addAppListListener((result, changedItemId) -> {

            if (adapter == null)
                setAdapter(result);
            else
                adapter.updateData(result, changedItemId);
        });

        binding.btnSortPublished.setOnClickListener(view -> adapter.sort(AppPreview.Order.PUBLISHED));
        binding.btnSortLastUpdated.setOnClickListener(view -> adapter.sort(AppPreview.Order.LAST_UPDATED));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setAdapter(List<AppPreview> apps) {

        if (binding != null) {
            adapter = new AppListAdapter(getContext(), apps, id -> {
                Bundle bundle = new Bundle();
                bundle.putString("appId", id);

                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_homeFragment_to_appFragment, bundle);
            });

            binding.rvAppList.setAdapter(adapter);
            binding.rvAppList.setLayoutManager(new LinearLayoutManager(getContext()));

//            IMPORTANT:
//            Uncomment when appList return only once
//            binding.btnSortPublished.performClick(); // perform click action works only visually
//            adapter.sort(AppPreview.Order.PUBLISHED); // default sort type, SettingsManager will handle this later
        }
    }


}
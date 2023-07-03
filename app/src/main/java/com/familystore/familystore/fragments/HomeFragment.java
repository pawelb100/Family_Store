package com.familystore.familystore.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.familystore.familystore.R;
import com.familystore.familystore.adapters.AppListAdapter;
import com.familystore.familystore.databinding.FragmentHomeBinding;
import com.familystore.familystore.listeners.database.AppListListener;
import com.familystore.familystore.listeners.lists.AppListClickListener;
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

        viewModel.addAppListListener(result -> {

            if (adapter == null) {

                adapter = new AppListAdapter(getContext(), result, id -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("appId", id);

                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_appFragment, bundle);
                });

                binding.rvAppList.setAdapter(adapter);
                binding.rvAppList.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            else
                adapter.updateData(result);



        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.removeAppListListener();
        binding = null;
    }
}
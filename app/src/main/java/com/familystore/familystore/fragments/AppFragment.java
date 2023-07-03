package com.familystore.familystore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.familystore.familystore.adapters.PictureListAdapter;
import com.familystore.familystore.databinding.FragmentAppBinding;
import com.familystore.familystore.listeners.database.SingleAppListener;
import com.familystore.familystore.listeners.database.UserListener;
import com.familystore.familystore.models.App;
import com.familystore.familystore.models.User;
import com.familystore.familystore.viewmodels.MainViewModel;
import com.squareup.picasso.Picasso;

public class AppFragment extends Fragment {

    private FragmentAppBinding binding;

    private MainViewModel viewModel;

    private PictureListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAppBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(MainViewModel.class);

        String id = getArguments().getString("appId");

        viewModel.getAppById(id, result -> {
            binding.name.setText(result.getName());

            viewModel.getUserById(result.getAuthorId(), result1 -> binding.author.setText("Autor: "+ result1.getName()));

            binding.version.setText("Wersja: "+result.getVersion());
            binding.description.setText(result.getDescription());

            if (!"".equals(result.getLogoUrl()))
                binding.changelogContent.setText("Programista nie podał tych informacji");
            else
                binding.changelogContent.setText(result.getChangelog());

            if (!"".equals(result.getLogoUrl()))
                Picasso.get()
                        .load(result.getLogoUrl())
                        .into(binding.logo);

            adapter = new PictureListAdapter(getContext(), result.getPictureUrls());
            binding.pictures.setAdapter(adapter);
            binding.pictures.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        });


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
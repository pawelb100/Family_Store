package com.familystore.familystore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.familystore.familystore.adapters.PictureListAdapter;
import com.familystore.familystore.databinding.FragmentAppBinding;
import com.familystore.familystore.utils.DateUtils;
import com.familystore.familystore.viewmodels.MainViewModel;
import com.squareup.picasso.Picasso;

public class AppFragment extends Fragment {

    private FragmentAppBinding binding;

    private MainViewModel viewModel;

    private PictureListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAppBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(MainViewModel.class);

        String id = getArguments().getString("appId");

        viewModel.getAppById(id, app -> {
            binding.name.setText(app.getName());
            binding.version.setText("Wersja: " + app.getVersion());
            binding.description.setText(app.getDescription());

            viewModel.getUserById(
                    app.getAuthorId(),
                    user -> binding.author.setText("Autor: " + user.getName())
            );
            // last updated
            if (app.getLastUpdated() == -1) {
                binding.lastUpdated.setVisibility(View.GONE);
            } else {
                binding.lastUpdated.setText("Ostatnia aktualizacja: " +
                        DateUtils.getDateStrFromEpochMilli(app.getLastUpdated()));
            }

            // changelog
            if (app.getChangelog().equals("")) {
                binding.changelogCard.setVisibility(View.GONE);
            } else {
                binding.changelogContent.setText(app.getChangelog());
            }

            // app logo
            if (!"".equals(app.getLogoUrl()))
                Picasso.get()
                        .load(app.getLogoUrl())
                        .into(binding.logo);

            // app pictures
            adapter = new PictureListAdapter(getContext(), app.getPictureUrls());
            binding.pictures.setAdapter(adapter);
            binding.pictures.setLayoutManager(new LinearLayoutManager(
                    getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                    ));
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
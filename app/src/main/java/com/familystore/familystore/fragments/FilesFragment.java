package com.familystore.familystore.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.familystore.familystore.R;
import com.familystore.familystore.databinding.FragmentFilesBinding;
import com.familystore.familystore.viewmodels.MainViewModel;

public class FilesFragment extends Fragment {

    private FragmentFilesBinding binding;

    private MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFilesBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(MainViewModel.class);
        
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
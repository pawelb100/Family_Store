package com.familystore.familystore.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.familystore.familystore.adapters.FileListAdapter;
import com.familystore.familystore.databinding.FragmentFilesBinding;
import com.familystore.familystore.utils.FileManager;
import com.familystore.familystore.viewmodels.MainViewModel;

public class FilesFragment extends Fragment {

    private FragmentFilesBinding binding;

    private MainViewModel viewModel;

    private FileManager fileManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFilesBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(MainViewModel.class);

        fileManager = new FileManager(getContext());

        FileListAdapter adapter = new FileListAdapter(getContext(), fileManager.getFiles(), file -> fileManager.installApk(file));
        binding.rvFileList.setAdapter(adapter);
        binding.rvFileList.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
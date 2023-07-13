package com.familystore.familystore.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.familystore.familystore.R;
import com.familystore.familystore.adapters.FileListAdapter;
import com.familystore.familystore.databinding.FragmentFilesBinding;
import com.familystore.familystore.listeners.lists.FileListClickListener;
import com.familystore.familystore.utils.FileManager;
import com.familystore.familystore.viewmodels.MainViewModel;

import java.io.File;

public class FilesFragment extends Fragment {

    private FragmentFilesBinding binding;

    private MainViewModel viewModel;

    private FileManager fileManager;

    private FileListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFilesBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(MainViewModel.class);

        fileManager = new FileManager(getContext());

        adapter = new FileListAdapter(getContext(), fileManager.getFiles(), new FileListClickListener() {
            @Override
            public void onClick(File file) {
                fileManager.installApk(file);
            }

            @Override
            public void onLongClick(File file, int position) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Czy chcesz to usunąć?")
                        .setMessage(file.getName())
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (fileManager.deleteFile(file)) {
                                    Toast.makeText(getContext(), "Plik usunięty", Toast.LENGTH_SHORT).show();
                                    refreshList(position);
                                }
                                else
                                    Toast.makeText(getContext(), "Plik nie został usunięty", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Nie", null)
                        .create().show();
            }
        });
        binding.rvFileList.setAdapter(adapter);
        binding.rvFileList.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void refreshList(int position) {
        if (adapter != null) {
            adapter.updateData(fileManager.getFiles());
        }
    }
}
package com.familystore.familystore.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.familystore.familystore.R;
import com.familystore.familystore.adapters.FileListAdapter;
import com.familystore.familystore.databinding.FragmentFilesBinding;
import com.familystore.familystore.listeners.lists.FileListClickListener;
import com.familystore.familystore.utils.FileManager;

import java.io.File;

public class FilesFragment extends Fragment {

    private FragmentFilesBinding binding;

    private FileManager fileManager;

    private FileListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFilesBinding.inflate(inflater, container, false);

        fileManager = new FileManager(getContext());

        adapter = new FileListAdapter(getContext(), fileManager.getFiles(), new FileListClickListener() {
            @Override
            public void onClick(File file) {
                fileManager.installApk(file);
            }

            @Override
            public void onLongClick(File file, int position) {

                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.delete_file_prompt))
                        .setMessage(file.getName())
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            if (fileManager.deleteFile(file)) {
                                Toast.makeText(getContext(), "Plik usunięty", Toast.LENGTH_SHORT).show();
                                refreshList(position);
                            }
                            else
                                Toast.makeText(getContext(), "Plik nie został usunięty", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
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
            adapter.removeItem(position);
        }
    }
}
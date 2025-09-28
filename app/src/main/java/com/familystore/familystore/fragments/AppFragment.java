package com.familystore.familystore.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.familystore.familystore.R;
import com.familystore.familystore.adapters.PictureListAdapter;
import com.familystore.familystore.databinding.FragmentAppBinding;
import com.familystore.familystore.utils.ApkDownloader;
import com.familystore.familystore.utils.BaseDateUtils;
import com.familystore.familystore.viewmodels.MainViewModel;
import com.squareup.picasso.Picasso;

public class AppFragment extends Fragment {

    private FragmentAppBinding binding;

    private PictureListAdapter adapter;

    private ApkDownloader apkDownloader;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAppBinding.inflate(inflater, container, false);

        FragmentActivity activity = requireActivity();
        ViewModelProvider viewModelProvider = new ViewModelProvider(activity);
        MainViewModel viewModel = viewModelProvider.get(MainViewModel.class);

        assert getArguments() != null;
        int id = getArguments().getInt("appId", -1);
        // id == -1 when opened using deep link
        if (id == -1) {
            Uri uri = activity.getIntent().getData();
            assert uri != null;
            assert uri.getLastPathSegment() != null;
            id = Integer.parseInt(uri.getLastPathSegment());
        }

        // check whether this fragment has been launched from the brand fragment
        // (to later disable author label click action)
        boolean isFromBrand = getArguments().getBoolean("fromBrand");

        viewModel.getAppById(id, app -> {
            // without this check application sometimes crashes when opened using deep links
            if (binding == null) {
                return;
            }
            // handle app with given id not being found (can occur when using deep links)
            if (app == null) {
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_appFragment_to_homeFragment);
                Toast.makeText(
                        getContext(),
                        getString(R.string.app_not_found),
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            binding.name.setText(app.getName());
            binding.version.setText(getString(
                    R.string.version_info,
                    app.getVersion()
            ));
            Picasso.get()
                    .load(app.getLogoUrl())
                    .into(binding.logo);

            binding.author.setText(getString(R.string.author_info, app.getBrand().name()));
            // brand onClick
            if (isFromBrand)
                binding.author.setClickable(false);
            else
                binding.author.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("brandId", app.getBrand().id());
                    bundle.putString("brandName", app.getBrand().name());
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_appFragment_to_brandAppsFragment, bundle);
                });

            if (app.getDescription() != null) {
                binding.description.setVisibility(View.VISIBLE);
                binding.description.setText(app.getDescription());
            }
            if (app.getLastUpdated() != null) {
                binding.lastUpdated.setVisibility(View.VISIBLE);
                binding.lastUpdated.setText(getString(
                        R.string.last_updated_date,
                        BaseDateUtils.getDateStrFromEpochMilli(
                                app.getLastUpdated().toInstant().toEpochMilli())
                ));
            }
            if (app.getChangelog() != null) {
                binding.changelogCard.setVisibility(View.VISIBLE);
                binding.changelogContent.setText(app.getChangelog());
            }

            // app pictures
            adapter = new PictureListAdapter(getContext(), app.getPictureUrls());
            binding.pictures.setAdapter(adapter);
            binding.pictures.setLayoutManager(new LinearLayoutManager(
                    getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            ));

            // app downloader
            if (apkDownloader == null) {
                apkDownloader = new ApkDownloader(
                        requireContext(), app.getDownloadUrl(), app.getName(), app.getVersion());
                binding.download.setOnClickListener(view -> {
                    apkDownloader.download();
                    Toast.makeText(
                            getContext(),
                            getString(R.string.download_started_info),
                            Toast.LENGTH_SHORT
                    ).show();
                    view.setEnabled(false);
                });
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
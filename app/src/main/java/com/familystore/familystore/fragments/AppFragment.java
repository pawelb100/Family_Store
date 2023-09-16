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

    private MainViewModel viewModel;

    private PictureListAdapter adapter;

    private ApkDownloader apkDownloader;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAppBinding.inflate(inflater, container, false);

        FragmentActivity activity = requireActivity();
        ViewModelProvider viewModelProvider = new ViewModelProvider(activity);
        viewModel = viewModelProvider.get(MainViewModel.class);

        assert getArguments() != null;
        String id = getArguments().getString("appId");
        // id == null when opened using deep link
        if (id == null) {
            Uri uri = activity.getIntent().getData();
            assert uri != null;
            id = uri.getLastPathSegment();
        }

        // check whether this fragment has been launched from the brand fragment
        // (to later disable author label click action)
        boolean isFromBrand = getArguments().getBoolean("fromBrand");

        viewModel.getAppById(id, app -> {
            // without this check application sometimes crashes when opened using deep links
            if (binding == null) {
                return;
            }
            // handle app with given id not being found (can occur when using deeplinks)
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
            binding.description.setText(app.getDescription());

            viewModel.getBrandById(
                    app.getAuthorId(),
                    brand -> {
                        binding.author.setText(getString(R.string.author_info, brand.getName()));
                        // brand onClick
                        if (isFromBrand)
                            binding.author.setClickable(false);
                        else
                            binding.author.setOnClickListener(view -> {
                                Bundle bundle = new Bundle();
                                bundle.putString("brandId", app.getAuthorId());
                                bundle.putString("brandName", brand.getName());
                                Navigation.findNavController(binding.getRoot())
                                        .navigate(R.id.action_appFragment_to_brandAppsFragment, bundle);
                            });
                    }
            );
            // last updated
            if (app.getLastUpdated() == -1) {
                binding.lastUpdated.setVisibility(View.GONE);
            } else {
                binding.lastUpdated.setText(getString(
                        R.string.last_updated_date,
                        BaseDateUtils.getDateStrFromEpochMilli(app.getLastUpdated())
                ));
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

            // app downloader
            if (apkDownloader == null)
                if (!app.getDownloadUrl().equals("")) {
                    apkDownloader = new ApkDownloader(requireContext(), app.getDownloadUrl(), app.getName(), app.getVersion());
                    binding.download.setOnClickListener(view -> {
                        apkDownloader.download();
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
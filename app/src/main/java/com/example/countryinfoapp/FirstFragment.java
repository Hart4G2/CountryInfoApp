package com.example.countryinfoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.countryinfoapp.databinding.FragmentFirstBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SearchView searchField;
    private RecyclerView listOfCountries;

    private CountryAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        searchField = binding.searchViewCountries;
        listOfCountries = binding.recyclerviewCountries;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listOfCountries.setLayoutManager(layoutManager);

        adapter = new CountryAdapter(new ArrayList<>(), getParentFragmentManager());
        listOfCountries.setAdapter(adapter);

        getAllCountries();

        searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getAllCountries(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return binding.getRoot();
    }

    private void getAllCountries() {
        String url = "https://restcountries.com/v3.1/all";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<Country> countries = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject countryObject = response.getJSONObject(i);
                            String countryName = countryObject.getJSONObject("name").getString("common");
                            String countryCode = countryObject.getString("cca2");

                            Country country = new Country(countryName, countryCode);
                            countries.add(country);
                        }

                        adapter.setCountries(new ArrayList<>(countries));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );

        // Add the request to the request queue.
        RequestQueue queue = Volley.newRequestQueue(this.requireContext());
        queue.add(request);
    }

    private void getAllCountries(String searchValue) {
        String url = String.format("https://restcountries.com/v3.1/name/%s", searchValue);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<Country> countries = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject countryObject = response.getJSONObject(i);
                            String countryName = countryObject.getJSONObject("name").getString("common");
                            String countryCode = countryObject.getString("cca2");

                            Country country = new Country(countryName, countryCode);
                            countries.add(country);
                        }

                        adapter.setCountries(new ArrayList<>(countries));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );



        // Add the request to the request queue.
        RequestQueue queue = Volley.newRequestQueue(this.requireContext());
        queue.add(request);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
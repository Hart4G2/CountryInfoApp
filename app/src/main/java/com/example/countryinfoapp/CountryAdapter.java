package com.example.countryinfoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private List<Country> countries;
    private FragmentManager fragmentManager;

    public CountryAdapter(List<Country> countries, FragmentManager fragmentManager) {
        this.countries = countries;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.bind(country);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("country_name", country.getName());

            SecondFragment secondFragment = new SecondFragment();
            secondFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, secondFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView codeTextView;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textview_country_name);
            codeTextView = itemView.findViewById(R.id.textview_country_code);
        }

        public void bind(Country country) {
            nameTextView.setText(country.getName());
            codeTextView.setText(country.getCountryCode());
        }
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}

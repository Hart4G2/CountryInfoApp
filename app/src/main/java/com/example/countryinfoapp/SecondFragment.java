package com.example.countryinfoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.countryinfoapp.databinding.FragmentSecondBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private TextView title;
    private TextView snippet;

    private static String selectedCountryName;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);

        title = binding.textviewTitle;
        snippet = binding.textviewSnippet;

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedCountryName = bundle.getString("country_name");
        }

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!selectedCountryName.isEmpty()){
            getCountryInfo(selectedCountryName);
        } else {
            title.setText("Ошибка. Не удалось получить информацию о стране.");
        }
    }

    private void getCountryInfo(String countryName) {
        String url = String.format("https://restcountries.com/v3.1/name/%s", countryName);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject countryObject = response.getJSONObject(0);

                        displayCountryInfo(countryObject);

                        title.setText(selectedCountryName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );

        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        queue.add(request);
    }

    public void displayCountryInfo(JSONObject countryObject) {
        StringBuilder sb = new StringBuilder();
        try {
            Iterator<String> keys = countryObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = countryObject.get(key);

                if (value instanceof JSONObject) {
                    sb.append(key).append(": ");
                    displayCountryInfo((JSONObject) value);
                }
                else if (value instanceof JSONArray) {
                    sb.append(key).append(": ");
                    JSONArray array = (JSONArray) value;
                    for (int i = 0; i < array.length(); i++) {
                        sb.append(array.get(i)).append(", ");
                    }
                    sb.setLength(sb.length() - 2);
                    sb.append("\n");
                }
                else {
                    sb.append(key).append(": ").append(value).append("\n");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        snippet.setText(sb.toString());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
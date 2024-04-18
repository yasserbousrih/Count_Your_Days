package com.example.counturdays.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.counturdays.LoginActivity;
import com.example.counturdays.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView messageTxt;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        messageTxt = root.findViewById(R.id.message);
        Button logoutButton = root.findViewById(R.id.LOGOUT);

        fetchQuote();


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);

                startActivity(intent);


            }
        });

        return root;
    }
    private void fetchQuote() {
        String url = "https://zenquotes.io/api/random";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse the JSON response
                        Gson gson = new Gson();
                        Quote[] quotes = gson.fromJson(response, Quote[].class);

                        // Set the text of messageTxt to the quote
                        messageTxt.setText(quotes[0].q);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                messageTxt.setText("Failed to fetch quote.");
            }
        });

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

}

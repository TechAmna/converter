package com.example.converter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;

public class Home_screen extends AppCompatActivity {
    Button button;
    TextView answer, temperature;
    TextInputEditText value;
    Spinner spinner, spinner2;
    double fromValue =0.0;
    String fromUnit = "";
    double toValue = 0.0;
    String toUnit = "";
    public Home_screen() {}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        // requestQueue = Volley.newRequestQueue(getApplicationContext());
        findID();
        temperature.setOnClickListener(view->{
            goToTemp();
        });
       button.setOnClickListener(view->{
           // get values
           try{
               fromValue=Double.parseDouble(Objects.requireNonNull(value.getText()).toString());
               fromUnit =spinner.getSelectedItem().toString();
               toUnit = spinner2.getSelectedItem().toString();
               convertValue();

           }catch (Exception e){
               Toast.makeText(this, Objects.requireNonNull(e.getMessage()).toUpperCase(),Toast.LENGTH_SHORT).show();
           }
       });





    }

    private void convertValue() {
        // logic of convert Currencies
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String baseUrl = "https://api.exchangerate-api.com/v4/latest/";
        String baseCurrency = "USD";  // Example base currency
        String apiKey = "ac883d2d39056c3fec23cbc2e3082475";
        String url = baseUrl + baseCurrency + "?apikey=" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rates = response.getJSONObject("rates");
                            saveRatesToFirebase(rates);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

// Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);



    }
    private void saveRatesToFirebase(JSONObject rates) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ratesRef = database.getReference("conversion_rates");

        Iterator<String> keys = rates.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Double rate = rates.getDouble(key);
                ratesRef.child("USD_to_" + key).setValue(rate);
                fetchConversionRate(fromUnit,toUnit);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void fetchConversionRate(String fromCurrency, String toCurrency) {
        DatabaseReference ratesRef = FirebaseDatabase.getInstance().getReference("conversion_rates");
        ratesRef.child(fromCurrency + "_to_" + toCurrency).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double rate = dataSnapshot.getValue(Double.class);
                if (rate != null) {
                    double amount = Double.parseDouble(value.getText().toString());
                    double result = amount * rate;
                    answer.setText(String.valueOf(result));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }



    public void findID(){
       value= findViewById(R.id.value);
       button = findViewById(R.id.button);
       answer = findViewById(R.id.answer);
       temperature = findViewById(R.id.temp);
       spinner = findViewById(R.id.toCurrencySpinner);
       spinner2 = findViewById(R.id.fromCurrencySpinner);

    }

    public void goToTemp() {
        Intent intent = new Intent(Home_screen.this,Temperature.class);
        startActivity(intent);

    }



}


package com.example.finale_weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView weatherRV;
    private RelativeLayout mainRL;
    private TextView cityNameTV,tempTV,conditionTV,humidityTV;
    private ProgressBar progressBar;
    private ImageView backIV,iconIV,searchIV;
    private TextInputEditText cityEdit;
    private ArrayList<WeatherRecyclerViewModal> weatherRecyclerViewModalArrayList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LocationManager locationManager;
    private int permission_code=1;
    private String cityName;
    FloatingActionButton locationFButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainRL=findViewById(R.id.idRLHome);
        cityNameTV=findViewById(R.id.idCityName);
        weatherRV=findViewById(R.id.idWeatherRecyclerview);
        tempTV=findViewById(R.id.idTVTemp);
        humidityTV=findViewById(R.id.idTVHumidity);
        conditionTV=findViewById(R.id.idTVCondition);
        progressBar=findViewById(R.id.dataLoadingProgressBar);
        cityEdit=findViewById(R.id.idtextFieldInput);
        backIV=findViewById(R.id.idBgImg);
        iconIV=findViewById(R.id.idTempIcon);
        searchIV=findViewById(R.id.idSearchImage);
        weatherRecyclerViewModalArrayList=new ArrayList<>();
        recyclerViewAdapter=new RecyclerViewAdapter(this,weatherRecyclerViewModalArrayList);
        weatherRV.setAdapter(recyclerViewAdapter);
        locationFButton=findViewById(R.id.idfbMap);




        setFloatingActionButtonPlusCurrentPosition(locationFButton);
        mainRL.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);


        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city=cityEdit.getText().toString();

                if(city.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "enter city", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    cityNameTV.setText(cityName);

                    getWeatherInfo(city);

                }
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int ID=item.getItemId();
        switch (ID)
        {
            case R.id.item2:
                finish();
                System.exit(0);
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    public void setFloatingActionButtonPlusCurrentPosition(View view)
    {

        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED);
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, permission_code);
        }
        Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null)
        {
            cityName=getCurrentCityName(location.getLongitude(),location.getLatitude());
            getWeatherInfo(cityName);
        }
        else
        {
            Toast.makeText(MainActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==permission_code)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Please provide Permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCurrentCityName(double longitude, double latitude)
    {
        String cityName="Not Found";
        Geocoder geocoder=new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addressList=geocoder.getFromLocation(latitude,longitude,4);


            for(Address itr: addressList)
            {
                if(itr!=null)
                {
                    String city=itr.getLocality();
                    if(city!=null&&!city.equals(""))
                    {
                        cityName=city;
                    }
                    else
                    {
                        Log.d("tag","City not found");
                        Toast.makeText(this, "City not Found!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return cityName;

    }

    private void getWeatherInfo(String cityName)
    {
        String url ="http://api.weatherapi.com/v1/forecast.json?key=63221b5c67a5459cb65125113210512&q="+cityName+"&days=1&aqi=yes&alerts=yes";

        cityNameTV.setText(cityName);
        cityEdit.setText("");
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
             //   Toast.makeText(MainActivity.this, "Inside", Toast.LENGTH_SHORT).show();

                weatherRecyclerViewModalArrayList.clear();// To clear data for previous city

                try {
                    String temp=response.getJSONObject("current").getString("temp_c");
                    tempTV.setText(temp+"°c");

                    int isDay=response.getJSONObject("current").getInt("is_day");
                    String condition =response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String humid=response.getJSONObject("current").getString("humidity");
                    String conditionIcon=response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
                    conditionTV.setText(condition);
                    humidityTV.setText("Humidity: "+humid+"%");

                    if(isDay==1)
                    {
                        Picasso.get().load("https://images.unsplash.com/photo-1470252649378-9c29740c9fa8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8bW9ybmluZ3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=800&q=60").into(backIV);
                    }
                    else
                    {
                        Picasso.get().load("https://images.unsplash.com/photo-1489549132488-d00b7eee80f1?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OXx8bmlnaHR8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60").into(backIV);

                    }


                    JSONObject forecastObj=response.getJSONObject("forecast");
                    JSONObject forecastArray=forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray jsonHourArray=forecastArray.getJSONArray("hour");


                    for(int i=0;i<jsonHourArray.length();i++)
                    {
                        JSONObject object=jsonHourArray.getJSONObject(i);
                        String time=object.getString("time");
                        String temperature=object.getString("temp_c");
                        String img=object.getJSONObject("condition").getString("icon");
                        String wind=object.getString("wind_kph");
                        weatherRecyclerViewModalArrayList.add(new WeatherRecyclerViewModal(time,temperature,img,wind));

                    }


                    recyclerViewAdapter.notifyDataSetChanged();





                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "City Not Found!", Toast.LENGTH_SHORT).show();
            }


        });


        requestQueue.add(jsonObjectRequest);

    }
}
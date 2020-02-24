package edu.asu.msse.akondama.assignment3ser423;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


/*
 * Copyright 2020 Aravinda Sai Kondamari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 *
 * @author Aravinda Sai Kondamari mailto:akondama@asu.edu
 * @version Febrary 10, 2020
 */
public class PlaceDescriptionActivity extends AppCompatActivity {
    PlaceLibrary placeLibrary;
    Integer position1, position2;
    TextView name;
    TextView category;
    TextView description;
    TextView addressTitle;
    TextView addressStreet;
    TextView elevation;
    TextView latitude;
    TextView longitude;
    TextView distance;
    TextView image;
    TextView initialBearing;
    Spinner spinner;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);
        name = findViewById(R.id.nameEntry);
        category = findViewById(R.id.categoryEntry);
        description = findViewById(R.id.descriptionEntry);
        addressTitle = findViewById(R.id.addressTitleEntry);
        addressStreet = findViewById(R.id.addressStreetEntry);
        elevation = findViewById(R.id.elevationEntry);
        latitude = findViewById(R.id.latitudeEntry);
        longitude = findViewById(R.id.longitudeEntry);
        spinner = findViewById(R.id.spinner);
        distance = findViewById(R.id.distanceEntry);
        initialBearing = findViewById(R.id.bearingEntry);
        image = findViewById(R.id.imageEntry);
        back = findViewById(R.id.backButton);

        Intent intent = getIntent();
        position1 = intent.getIntExtra("position", 0);
        placeLibrary = (PlaceLibrary) intent.getSerializableExtra("placeLibrary");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, placeLibrary.getPlaceNames());
        spinner.setAdapter(adapter);
        PlaceDescription place = placeLibrary.getPlaces().get(position1);
        name.setText(place.getName());
        description.setText(place.getDescription());
        category.setText(place.getCategory());
        addressTitle.setText(place.getAddressTitle());
        addressStreet.setText(place.getAddressStreet());
        elevation.setText(Double.toString(place.getElevation()));
        latitude.setText(Double.toString(place.getLatitude()));
        longitude.setText(Double.toString(place.getLongitude()));
        image.setText(place.getImage());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateDistance(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("result", placeLibrary);
                    setResult(RESULT_OK, intent);
                    finish();
            }
        });
    }

    private void calculateDistance(int position) {
        position2 = position;
        PlaceDescription place1 = placeLibrary.getPlaces().get(position1);
        PlaceDescription place2 = placeLibrary.getPlaces().get(position2);
        String gcd = greatCircleDistance(place1.getLatitude(), place1.getLongitude(), place2.getLatitude(), place2.getLongitude());
        String bearing = initialBearing(place1.getLatitude(), place1.getLongitude(), place2.getLatitude(), place2.getLongitude());
        initialBearing.setText(bearing + " Degrees");
        distance.setText(gcd + " KMs");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_delete_app_bar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("result", placeLibrary);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            placeLibrary = (PlaceLibrary) data.getSerializableExtra("result");
            PlaceDescription place = placeLibrary.getPlaces().get(position1);
            name.setText(place.getName());
            description.setText(place.getDescription());
            category.setText(place.getCategory());
            addressTitle.setText(place.getAddressTitle());
            addressStreet.setText(place.getAddressStreet());
            elevation.setText(Double.toString(place.getElevation()));
            latitude.setText(Double.toString(place.getLatitude()));
            longitude.setText(Double.toString(place.getLongitude()));
            image.setText(place.getImage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.editPlace) {
            Intent intent = new Intent(this, EditPlaceActivity.class);
            intent.putExtra("position", position1);
            intent.putExtra("placeLibrary", placeLibrary);
            intent.putExtra("flag", true);
            startActivityForResult(intent,1);
        }
        if (id == R.id.deletePlace) {
            ArrayList<PlaceDescription> pla = placeLibrary.getPlaces();
            pla.remove(position1.intValue());
            placeLibrary.setPlaces(pla);
            Log.d("kkk", "1. " + placeLibrary.getPlaceNames().toString());
            Intent intent = new Intent();
            intent.putExtra("result", placeLibrary);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public String greatCircleDistance(double latitude1, double longitude1, double latitude2, double longitude2 ){

        double R = 6371;
        latitude1 = Math.toRadians(latitude1);
        longitude1 = Math.toRadians(longitude1);

        latitude2 = Math.toRadians(latitude2);
        longitude2 = Math.toRadians(longitude2);

        double x = (longitude2 - longitude1);
        double y = (latitude2 - latitude1);
        double c = Math.pow(Math.sin(y/2),2) + Math.cos(latitude1) * Math.cos(latitude2) * Math.pow(Math.sin(x/2),2);

        double distance = 2 * Math.asin(Math.sqrt(c));
        return (String.format("%.3f",distance*R));
    }

    public String initialBearing(double latitude1, double longitude1, double latitude2, double longitude2){

        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return String.format("%.2f",(Math.toDegrees(Math.atan2(y, x))+360)%360);
    }
}

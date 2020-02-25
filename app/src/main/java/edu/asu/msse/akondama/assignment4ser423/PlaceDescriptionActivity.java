package edu.asu.msse.akondama.assignment4ser423;

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

import edu.asu.msse.akondama.assignment4ser423.JsonRPC.AsyncCollectionConnect;
import edu.asu.msse.akondama.assignment4ser423.JsonRPC.MethodInformation;


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
 * @version Febrary 24, 2020
 */

/**
 * Purpose: Displays individual Place Description selected from MainActivity.
 * Provides delete and edit options. Edit options starts the EditPlaceActivity in edit mode.
 *
 * Makes changes to the data at JsonRPCServer asynchronously via AsyncCollectionConnect.
 */
public class PlaceDescriptionActivity extends AppCompatActivity {
    String place1, place2;
    public TextView name;
    public TextView category;
    public TextView description;
    public TextView addressTitle;
    public TextView addressStreet;
    public TextView elevation;
    public TextView latitude;
    public TextView longitude;
    public TextView distance;
    public TextView image;
    public TextView initialBearing;
    public Spinner spinner;
    public Button back;
    public ArrayList<String> places;

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
        place1 = intent.getStringExtra("selected");
        places = intent.getStringArrayListExtra("places");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                place2 = places.get(position);
                calculateDistance(place2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });
    }

    private void calculateDistance(String place2) {
        MethodInformation mi = new MethodInformation(this, this.getString(R.string.defaultUrl),"get",
                new Object[]{place2, "true", latitude.getText(), longitude.getText()});
        AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MethodInformation mi = new MethodInformation(this, this.getString(R.string.defaultUrl),"get",
                new Object[]{place1, "false"});
        AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_delete_app_bar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.editPlace) {
            String name1 = name.getText().toString();
            String category1 = category.getText().toString();
            String description1 = description.getText().toString();
            String addressTitle1 = addressTitle.getText().toString();
            String addressStreet1 = addressStreet.getText().toString();
            String elevation1 = elevation.getText().toString();
            String latitude1 = latitude.getText().toString();
            String longitude1 = longitude.getText().toString();
            PlaceDescription placeDescription = new PlaceDescription();
            placeDescription.setName(name1);
            placeDescription.setDescription(description1);
            placeDescription.setCategory(category1);
            placeDescription.setAddressTitle(addressTitle1);
            placeDescription.setAddressStreet(addressStreet1);
            placeDescription.setImage(name1);
            placeDescription.setElevation(Double.parseDouble(elevation1));
            placeDescription.setLatitude(Double.parseDouble(latitude1));
            placeDescription.setLongitude(Double.parseDouble(longitude1));
            Intent intent = new Intent(this, EditPlaceActivity.class);
            intent.putExtra("placeDescription", placeDescription);
            intent.putExtra("flag", true);
            startActivity(intent);
        }
        if (id == R.id.deletePlace) {
            MethodInformation mi = new MethodInformation(this, this.getString(R.string.defaultUrl),"remove",
                    new Object[]{place1, true});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        }
        return super.onOptionsItemSelected(item);
    }
}

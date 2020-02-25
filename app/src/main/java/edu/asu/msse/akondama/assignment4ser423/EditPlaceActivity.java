package edu.asu.msse.akondama.assignment4ser423;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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
 * Purpose: This is the activity used to Add or edit a place in the list of places already available.
 *
 * This activity initializes in edit mode from the PlaceDescriptionActivity and
 * in Add mode from MainActivity.
 *
 * Makes changes to the data at JsonRPCServer asynchronously via AsyncCollectionConnect.
 */

public class EditPlaceActivity extends AppCompatActivity {
    EditText name;
    EditText category;
    EditText description;
    EditText addressTitle;
    EditText addressStreet;
    EditText elevation;
    EditText latitude;
    EditText longitude;

    Button save;

    PlaceDescription placeDescription;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);
        flag = getIntent().getBooleanExtra("flag", false);
        name = findViewById(R.id.nameEntry);
        category = findViewById(R.id.categoryEntry);
        description = findViewById(R.id.descriptionEntry);
        addressTitle = findViewById(R.id.addressTitleEntry);
        addressStreet = findViewById(R.id.addressStreetEntry);
        elevation = findViewById(R.id.elevationEntry);
        latitude = findViewById(R.id.latitudeEntry);
        longitude = findViewById(R.id.longitudeEntry);
        save = findViewById(R.id.saveButton);

        if(flag){
            placeDescription = (PlaceDescription) getIntent().getSerializableExtra("placeDescription");
            name.setText(placeDescription.getName());
            //name.setClickable(false);
            name.setEnabled(false);
            category.setText(placeDescription.getCategory());
            description.setText(placeDescription.getDescription());
            addressTitle.setText(placeDescription.getAddressTitle());
            addressStreet.setText(placeDescription.getAddressStreet());
            elevation.setText(placeDescription.getElevation()+"");
            latitude.setText(placeDescription.getLatitude()+"");
            longitude.setText(placeDescription.getLongitude()+"");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean flag = performChecks();
                if(flag){
                    saveDetails();
                    finish();
                }else{
                    Toast.makeText(EditPlaceActivity.this, "All Fields are Mandatory and last 3 fields only accepts Numbers.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveDetails() {
        if(placeDescription == null){
            Toast.makeText(EditPlaceActivity.this, "Please enter the details.", Toast.LENGTH_SHORT).show();
        }
        if(flag){
            MethodInformation mi = new MethodInformation(this, this.getString(R.string.defaultUrl),"remove",
                    new Object[]{placeDescription.getName(), false});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
            mi = new MethodInformation(this, this.getString(R.string.defaultUrl),"add",
                    new Object[]{placeDescription.toJson()});
            ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        }else {
            MethodInformation mi = new MethodInformation(this, this.getString(R.string.defaultUrl),"add",
                    new Object[]{placeDescription.toJson()});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        }
    }

    private Boolean performChecks(){
        String name1 = name.getText().toString();
        String category1 = category.getText().toString();
        String description1 = description.getText().toString();
        String addressTitle1 = addressTitle.getText().toString();
        String addressStreet1 = addressStreet.getText().toString();
        String elevation1 = elevation.getText().toString();
        String latitude1 = latitude.getText().toString();
        String longitude1 = longitude.getText().toString();
        if(StringUtils.isBlank(name1)){
            return false;
        }
        if(StringUtils.isBlank(category1)){
            return false;
        }
        if(StringUtils.isBlank(description1)){
            return false;
        }
        if(StringUtils.isBlank(addressTitle1)){
            return false;
        }
        if(StringUtils.isBlank(addressStreet1)){
            return false;
        }
        if(StringUtils.isBlank(elevation1) || !NumberUtils.isCreatable(elevation1)){
            return false;
        }
        if(StringUtils.isBlank(longitude1) || !NumberUtils.isCreatable(longitude1)){
            return false;
        }
        if(StringUtils.isBlank(latitude1 )|| !NumberUtils.isCreatable(latitude1)){
            return false;
        }
        placeDescription = new PlaceDescription();
        placeDescription.setName(name1);
        placeDescription.setDescription(description1);
        placeDescription.setCategory(category1);
        placeDescription.setAddressTitle(addressTitle1);
        placeDescription.setAddressStreet(addressStreet1);
        placeDescription.setImage(name1);
        placeDescription.setElevation(Double.parseDouble(elevation1));
        placeDescription.setLatitude(Double.parseDouble(latitude1));
        placeDescription.setLongitude(Double.parseDouble(longitude1));
        return true;
    }
}

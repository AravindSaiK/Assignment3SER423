package edu.asu.msse.akondama.assignment3ser423;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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

public class EditPlaceActivity extends AppCompatActivity {

    PlaceLibrary placeLibrary;
    Integer position;

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
        placeLibrary = (PlaceLibrary) getIntent().getSerializableExtra("placeLibrary");
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
            position = getIntent().getIntExtra("position", 0);
            PlaceDescription place = placeLibrary.getPlaces().get(position);
            name.setText(place.getName());
            //name.setClickable(false);
            name.setEnabled(false);
            category.setText(place.getCategory());
            description.setText(place.getDescription());
            addressTitle.setText(place.getAddressTitle());
            addressStreet.setText(place.getAddressStreet());
            elevation.setText(place.getElevation()+"");
            latitude.setText(place.getLatitude()+"");
            longitude.setText(place.getLongitude()+"");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean flag = performChecks();
                if(flag){
                    saveDetails();
                    Intent intent = new Intent();
                    intent.putExtra("result", placeLibrary);
                    setResult(RESULT_OK, intent);
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
            placeLibrary.getPlaces().add(position.intValue(), placeDescription);
            placeLibrary.getPlaces().remove(position.intValue() + 1);
        }else {
            placeLibrary.getPlaces().add(placeDescription);
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

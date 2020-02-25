package edu.asu.msse.akondama.assignment4ser423;

import org.json.JSONObject;

import java.io.Serializable;

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
 * Bean class for the PlaceDescription.
 */
public class PlaceDescription implements Serializable {

    private String name;
    private String description;
    private String category;
    private String addressTitle;
    private String addressStreet;
    private String image;
    private Double elevation;
    private Double latitude;
    private Double longitude;

    public PlaceDescription() {
    }

    public PlaceDescription(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            this.setName(json.getString("name"));
            this.setDescription(json.getString("description"));
            this.setCategory(json.getString("category"));
            this.setAddressTitle(json.getString("address-title"));
            this.setAddressStreet(json.getString("address-street"));
            this.setElevation(json.getDouble("elevation"));
            this.setLatitude(json.getDouble("latitude"));
            this.setLongitude(json.getDouble("longitude"));
            this.setImage(json.getString("image"));
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "Error converting from json");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("elevation", elevation);
            jsonObject.put("name", this.name);
            jsonObject.put("image", this.image);
            jsonObject.put("address-title", this.addressTitle);
            jsonObject.put("address-street", this.addressStreet);
            jsonObject.put("description", this.description);
            jsonObject.put("category", this.category);
        } catch (Exception var3) {
            System.out.println("Exception: " + var3.getMessage());
        }

        return jsonObject;
    }
}

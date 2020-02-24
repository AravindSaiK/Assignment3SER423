package edu.asu.msse.akondama.assignment4ser423;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

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
public class PlaceLibrary implements Serializable {

    ArrayList<PlaceDescription> places;

    public PlaceLibrary(String placeJson) {
        JSONObject json;
        try {
            json = new JSONObject(placeJson);
            Iterator<String> itr = json.keys();
            while(itr.hasNext()){
                String place = json.getString(itr.next());
                PlaceDescription placeDescription = new PlaceDescription(place);
                addPlaces(placeDescription);
            }
        } catch (JSONException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "Error converting from json in PlaceLibrary");
        }
    }

    public void setPlaces(ArrayList<PlaceDescription> places) {
        this.places = places;
    }

    public void addPlaces(PlaceDescription placeDescription){
        if(places == null){
            places = new ArrayList<>();
        }
        places.add(placeDescription);
    }

    public ArrayList<PlaceDescription> getPlaces() {
        return places;
    }

    public ArrayList<String> getPlaceNames(){
        ArrayList<String> placeNames = new ArrayList<>();
        if(places != null){
            for (PlaceDescription pd: places) {
                placeNames.add(pd.getName());
            }
        }
        return placeNames;
    }
}

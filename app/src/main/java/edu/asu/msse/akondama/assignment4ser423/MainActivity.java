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
import android.widget.ListView;

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
 * Purpose: This is the UI that the app is initialized with.
 * Displays a list of Places that can be selected to view their individual details in
 * PlaceDescriptionActivity and also gives an option to add a place from EditPlaceActivity.
 *
 * Calls and loads data from JsonRPCServer asynchronously accessed from AsyncCollectionConnect.
 */
public class MainActivity extends AppCompatActivity {

    public ListView placeList;
    public ArrayList<String> places;
    public ArrayAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placeList = findViewById(R.id.placeList);
        places = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addPlace) {
            Intent intent = new Intent(this, EditPlaceActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);
        placeList.setAdapter(listAdapter);
        placeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendToPlaceDescriptionActivity(position);
            }
        });
        getNamesFromRPCServer();
        Log.d("namesmain",this.toString());
    }

    private void sendToPlaceDescriptionActivity(int position) {
        String selected = places.get(position);
        Log.d("Selected: ", selected);
        Intent intent = new Intent(this, PlaceDescriptionActivity.class);
        intent.putExtra("selected", selected);
        intent.putExtra("places", places);
        startActivity(intent);
    }

    private void getNamesFromRPCServer() {
        MethodInformation mi = new MethodInformation(this, this.getString(R.string.defaultUrl),"getNames",
                new Object[]{});
        AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
    }
}

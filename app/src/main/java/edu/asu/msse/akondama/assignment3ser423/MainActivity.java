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
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
public class MainActivity extends AppCompatActivity {

    ListView placeList;
    PlaceLibrary placeLibrary;
    ArrayList<String> places;
    ArrayAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placeList = findViewById(R.id.placeList);
        
        String placeJson = getFileFromRaw("places");
        placeLibrary = new PlaceLibrary(placeJson);
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
            intent.putExtra("placeLibrary", placeLibrary);
            startActivityForResult(intent,1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        places = placeLibrary.getPlaceNames();

        listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);
        placeList.setAdapter(listAdapter);

        placeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendToPlaceDescriptionActivity(position);
            }
        });
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(data!= null) {
                placeLibrary = (PlaceLibrary) data.getSerializableExtra("result");
            }
        }
        Log.d("kkk", "2. " + placeLibrary.getPlaceNames().toString());
    }

    private void sendToPlaceDescriptionActivity(int position) {
        Intent intent = new Intent(this, PlaceDescriptionActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("placeLibrary", placeLibrary);
        startActivityForResult(intent,1);
    }

    private String getFileFromRaw(String places) {
        InputStream iStream = getResources().openRawResource(R.raw.places);
        ByteArrayOutputStream byteStream = null;
        try {
            byte[] buffer = new byte[iStream.available()];
            iStream.read(buffer);
            byteStream = new ByteArrayOutputStream();
            byteStream.write(buffer);
            byteStream.close();
            iStream.close();
        } catch (IOException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "Error reading from file.");
        }
        return byteStream.toString();
    }
}

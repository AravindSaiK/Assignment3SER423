package edu.asu.msse.akondama.assignment4ser423.JsonRPC;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

import edu.asu.msse.akondama.assignment4ser423.MainActivity;
import edu.asu.msse.akondama.assignment4ser423.PlaceDescription;
import edu.asu.msse.akondama.assignment4ser423.PlaceDescriptionActivity;

/*
 * Copyright (c) 2020 Tim Lindquist,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu,     Aravinda Sai Kondamari akondama@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version February 24, 2020
 */

/**
 * This class extends AsyncTask and override the onPreExecute, doInBackground, onPostExecute methods
 * to connect to JsonRPCServer and get the results to populate the UI Asynchronously
 * without affecting the UI while waiting for the results.
 */

// AsyncTask. Generic type parameters:
//    first is base type of array input to doInBackground method
//    second is type for input to onProgressUpdate method. also base type of an array
//    third is the return type for the doInBackground method, whose value is the argument
//          to the onPostExecute method.
public class AsyncCollectionConnect extends AsyncTask<MethodInformation, Integer, MethodInformation> {

    @Override
    protected void onPreExecute(){
        android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
    }

    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest){
        // array of methods to be called. Assume exactly one input, a single MethodInformation object
        android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        try {
            JSONArray ja = new JSONArray(aRequest[0].params);
            android.util.Log.d(this.getClass().getSimpleName(),"params: "+ja.toString());
            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+aRequest[0].method+"\", \"params\":"+ja.toString()+
                    ",\"id\":3}";
            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(aRequest[0].urlString)), aRequest[0].parent);
            aRequest[0].resultAsJson = conn.call(requestData);
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                    ex.getMessage());
            ex.printStackTrace();
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformation res){
        /*
        * Using AsyncTask is constraining in the following sense: Either you create a separate Class
        * extending AsyncTask for each call (or method being called), or you must determine which
        * (and possibly where) the method was called in onPostExecute. Thus, the if-then-else below.
        * In iOS we use a different approach by passing the block of code to be executed upon completion
        * of the asynchronous call. While Java has lambda's, they are constrained such that doing so is
        * not a viable option at this time.
        * Another approach would be to define an interface and have the calling class implement the
        * methods of the interface. This would abbreviate the code below while requiring the caller to
        * define what are commonly called delegate methods, or call-backs which could be called by
        * onPostExecute.
        * If you are thinking: Why didn't the designers of Android define the View Objects to be shared
        * (thread-safe) objects, thus allowing multiple threads to access them, each getting the "key"
        * before reading or making changes. The problem with this approach is that the view objects would
        * have to relinquish control to threads other than the UI Thread. This could cause the user to
        * experience non-responsiveness of the UI/App if misused by the App Developer.
        */
        android.util.Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        android.util.Log.d(this.getClass().getSimpleName(), " resulting is: " + res.resultAsJson);
        try {
            if (res.method.equals("getNames")) {
                JSONObject jo = new JSONObject(res.resultAsJson);
                JSONArray ja = jo.getJSONArray("result");
                MainActivity parent = (MainActivity) res.parent;
                Log.d("names",parent.toString());
                parent.listAdapter.clear();
                parent.places.clear();
                for (int i = 0; i < ja.length(); i++) {
                    parent.places.add(ja.getString(i));
                }

                parent.listAdapter.notifyDataSetChanged();
            } else if (res.method.equals("get")) {
                String distance = (String) res.params[1];
                JSONObject jo = new JSONObject(res.resultAsJson);
                PlaceDescription place = new PlaceDescription(jo.getJSONObject("result").toString());
                PlaceDescriptionActivity parent = (PlaceDescriptionActivity) res.parent;
                if(StringUtils.endsWithIgnoreCase(distance, "false")) {
                    parent.name.setText(place.getName());
                    parent.description.setText(place.getDescription());
                    parent.category.setText(place.getCategory());
                    parent.addressTitle.setText(place.getAddressTitle());
                    parent.addressStreet.setText(place.getAddressStreet());
                    parent.elevation.setText(Double.toString(place.getElevation()));
                    parent.latitude.setText(Double.toString(place.getLatitude()));
                    parent.longitude.setText(Double.toString(place.getLongitude()));
                    parent.image.setText(place.getImage());
                }else{
                    Double lat1 = Double.parseDouble(res.params[2].toString());
                    Double lon1 = Double.parseDouble(res.params[3].toString());
                    Double lat2 = place.getLatitude();
                    Double lon2 = place.getLongitude();
                    String gcd = greatCircleDistance(lat1, lon1, lat2, lon2);
                    String ib = initialBearing(lat1, lon1, lat2, lon2);
                    parent.initialBearing.setText(ib + " Degrees");
                    parent.distance.setText(gcd + " KMs");
                }
            } else if (res.method.equals("remove")){
                boolean flag = (boolean) res.params[1];
                if(flag) {
                    res.parent.finish();
                }
            }else if (res.method.equals("add")){
                res.parent.finish();
            }
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"Exception: "+ex.getMessage());
        }
    }

    public String greatCircleDistance(Double latitude1, Double longitude1, Double latitude2, Double longitude2 ){

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

    public String initialBearing(Double latitude1, Double longitude1, Double latitude2, Double longitude2){

        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return String.format("%.2f",(Math.toDegrees(Math.atan2(y, x))+360)%360);
    }
}
package edu.asu.msse.akondama.assignment4ser423.JsonRPC;

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
 *  Purpose: This is a bean class that is used to form the request and responses that is used in
 *  AsyncCollectionConnect to connect and get details from JsonRPCServer.
 */

import androidx.appcompat.app.AppCompatActivity;

public class MethodInformation {
    public String method;
    public Object[] params;
    public AppCompatActivity parent;
    public String urlString;
    public String resultAsJson;

    public MethodInformation(AppCompatActivity parent, String urlString, String method, Object[] params){
        this.method = method;
        this.parent = parent;
        this.urlString = urlString;
        this.params = params;
        this.resultAsJson = "{}";
    }
}

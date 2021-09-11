package com.swapnil.mobik.Repositry;


import android.text.TextUtils;
import android.util.Log;

import com.swapnil.mobik.Model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class JSONParser {

    private static final String LOG_TAG = "JSONParser";

    public static ArrayList<Image>[] fetchData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        ArrayList<Image> [] listOfImages = extractFeatureFromJson(jsonResponse);

        return listOfImages;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the  JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * 0 index = Full size
     * 1 index = thumb size
     *
     * */
    private static ArrayList<Image>[] extractFeatureFromJson(String imageJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(imageJson)) {
            return null;
        }
        ArrayList<Image> fullImageList = new ArrayList<>();
        ArrayList<Image> thumbImageList = new ArrayList<>();
        ArrayList<Image>[] imagesList = new ArrayList[2];
        imagesList[0] = fullImageList;
        imagesList[1] = thumbImageList;
        try {
            // Create a JSONObject from the JSON response string
            JSONArray baseJsonResponse = new JSONArray(imageJson);

            for (int i = 0; i < baseJsonResponse.length(); i++) {

                JSONObject currentImage = baseJsonResponse.getJSONObject(i);
                JSONObject url = currentImage.getJSONObject("urls");
                String fullImage = url.getString("full");
                String thumbImage = url.getString("small");

                Image fullI = new Image(fullImage);
                Image thumbI = new Image(thumbImage);
                imagesList[0].add(fullI);
                imagesList[1].add(thumbI);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the  JSON results", e);
        }

        Log.d(LOG_TAG, "extractFeatureFromJson: 1: "+imagesList[0].size()+" 2: "+imagesList[1].size());
        // Return the array of images
        return imagesList;
    }
}

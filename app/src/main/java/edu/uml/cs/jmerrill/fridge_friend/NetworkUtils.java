package edu.uml.cs.jmerrill.fridge_friend;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public final class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String UPC_DB_KEY = "f7a8e84f3b341db3d2f8c3bff9435931";

    private static final String REQUEST_URL = "http://api.upcdatabase.org/json/";

    private NetworkUtils() { }

    public static UpcItem fetchUpcItem(String code) {
        String urlString = REQUEST_URL + UPC_DB_KEY + "/" + code;

        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request ", e);
        }

        UpcItem upcItem = extractFeatureFromJson(jsonResponse);
        return upcItem;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        if (url == null)
            return null;

        URLConnection urlConnection = url.openConnection();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()));

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();

        return stringBuilder.toString();
    }

    private static UpcItem extractFeatureFromJson(String upcItemJSON) {
        // Check if JSON string is empty or null
        if(TextUtils.isEmpty(upcItemJSON)) {
            return null;
        }

        UpcItem upcItem = new UpcItem("not-found", "not-found");

        try {
            JSONObject baseJsonResponse = new JSONObject(upcItemJSON);

            upcItem = new UpcItem(baseJsonResponse.getString("description"),
                    baseJsonResponse.getString("number"));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }

        return upcItem;
    }
}


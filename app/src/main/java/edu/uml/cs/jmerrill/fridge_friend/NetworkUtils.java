package edu.uml.cs.jmerrill.fridge_friend;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public final class NetworkUtils {

    // Tag to reference class in debug log
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    // API keys
    private static final String UPC_DB_KEY = "f7a8e84f3b341db3d2f8c3bff9435931";
    private static final String GOOGLE_KEY = "AIzaSyDheIQEbgnR11htdXKOMJj3D5xLfceHgV4";

    // base URLs used to construct Http requests
    private static final String UPC_REQUEST_URL = "http://api.upcdatabase.org/json/";
    private static final String SEARCH_REQUEST_URL = "https://www.googleapis.com/customsearch/v1";

    // ID of our Google Custom Search Engine
    // Conducts image searches for products throughout the web
    private static final String CSE = "005068381006029258478:me6cxr6zfau";

    private NetworkUtils() { }

    // construct and return a UpcItem to the loader
    public static UpcItem fetchUpcItem(String code) {

        String urlString = UPC_REQUEST_URL + UPC_DB_KEY + "/" + code;
        Log.d(LOG_TAG, urlString);

        URL upcUrl = null, thumbnailUrl = null;

        try {
            upcUrl = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the UPC URL ", e);
        }

        // obtain UPC data from Http request
        String upcJSON = null;

        try {
            upcJSON = makeHttpRequest(upcUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the UPC HTTP request ", e);
        }

        UpcItem upcItem = extractUPCFromJson(upcJSON);

        // obtain thumbnail if the UpcItem was found
        if(upcItem != null) {
            try {
                thumbnailUrl = new URL(buildThumbnailUrl(upcItem.getName()));
                String thumbnailJSON = makeHttpRequest(thumbnailUrl);
                byte[] thumbnail = extractThumbnailFromJSON(thumbnailJSON);
                upcItem.setThumbnail(thumbnail);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Problem building the thumbnail URL ", e);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the thumbnail HTTP request ", e);
            }

        }

        Log.d(LOG_TAG, "UpcItem constructed");
        return upcItem;
    }

    // create a URL for the image search request
    private static String buildThumbnailUrl(String name) {
        Uri baseUri = Uri.parse(SEARCH_REQUEST_URL);
        String url = null;

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("key", GOOGLE_KEY);
        uriBuilder.appendQueryParameter("cx", CSE);

        // lose leading spaces in the name string
        uriBuilder.appendQueryParameter("q", name.substring(2));

        // set option for image search
        uriBuilder.appendQueryParameter("searchType", "image");

        // set option for JPEG format
        uriBuilder.appendQueryParameter("fileType", "jpg");

        // return only small images
        uriBuilder.appendQueryParameter("imgSize", "small");

        // return 2 results (the 1st image result is often from a news article)
        uriBuilder.appendQueryParameter("num", "2");

        Log.d(LOG_TAG, uriBuilder.toString());

        url = uriBuilder.toString();

        return url;
    }

    // returns a JSON string retrieved from a URL
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

    // JSON parser for the UPCItem string fields
    private static UpcItem extractUPCFromJson(String upcItemJSON) {
        // Check if JSON string is empty or null
        if(TextUtils.isEmpty(upcItemJSON)) {
            return null;
        }

        // fallback name and ID values in case no JSON values are found
        UpcItem upcItem = new UpcItem("not-found", "not-found");

        try {
            JSONObject baseJsonResponse = new JSONObject(upcItemJSON);

            upcItem = new UpcItem(baseJsonResponse.getString("description"),
                    baseJsonResponse.getString("number"));


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the UPC JSON results", e);
        }

        Log.d(LOG_TAG, "Item Name: " + upcItem.getName());
        Log.d(LOG_TAG, "Item UPC#: " + upcItem.getId());
        return upcItem;
    }

    // JSON parser for CSE image search results
    // returns a byte array of the resulting image
    private static byte[] extractThumbnailFromJSON(String thumbnailUrl) {
        if(TextUtils.isEmpty(thumbnailUrl)) {
            return null;
        }

        byte[] thumbnail = null;
        try {
            // navigate to 2nd image result and get link
            JSONObject baseJsonResponse = new JSONObject(thumbnailUrl);

            JSONArray imageArray = baseJsonResponse.getJSONArray("items");
            JSONObject imageInfo = imageArray.getJSONObject(1);
            String link = imageInfo.getString("link");
            Log.d(LOG_TAG, "link = " + link);
            InputStream imageSource;

            imageSource = (InputStream) new URL(link).getContent();

            // read bytes from the InputStream into a byte array
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int len;
            byte[] chunk = new byte[16384];

            while ((len = imageSource.read(chunk, 0, chunk.length)) != -1) {
                buffer.write(chunk, 0, len);
            }

            buffer.flush();

            thumbnail = buffer.toByteArray();
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the thumbnail JSON results", e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return thumbnail;
    }
}


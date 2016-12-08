package edu.uml.cs.jmerrill.fridge_friend;

import android.graphics.drawable.Drawable;
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

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    //private static final String UPC_DB_KEY = "467287e945bf7c1faad565a46139211f";
    private static final String UPC_DB_KEY = "f7a8e84f3b341db3d2f8c3bff9435931";


    private static final String GOOGLE_KEY = "AIzaSyDheIQEbgnR11htdXKOMJj3D5xLfceHgV4";

    private static final String UPC_REQUEST_URL = "http://api.upcdatabase.org/json/";

    private static final String SEARCH_REQUEST_URL = "https://www.googleapis.com/customsearch/v1";

    private static final String CSE = "005068381006029258478:me6cxr6zfau";

    private NetworkUtils() { }

    public static UpcItem fetchUpcItem(String code) {
        String urlString = UPC_REQUEST_URL + UPC_DB_KEY + "/" + code;
        Log.d(LOG_TAG, urlString);

        URL upcUrl = null, thumbnailUrl = null;

        try {
            upcUrl = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the UPC URL ", e);
        }

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(upcUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the UPC HTTP request ", e);
        }

        UpcItem upcItem = extractUPCFromJson(jsonResponse);

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
        Log.d(LOG_TAG, "made the UPC item!");
        return upcItem;
    }

    private static String buildThumbnailUrl(String name) {
        Uri baseUri = Uri.parse(SEARCH_REQUEST_URL);
        String url = null;
        Log.d(LOG_TAG, "name of thumb: " + name);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("key", GOOGLE_KEY);
        uriBuilder.appendQueryParameter("cx", CSE);
        uriBuilder.appendQueryParameter("q", name);
        uriBuilder.appendQueryParameter("searchType", "image");
        uriBuilder.appendQueryParameter("fileType", "jpg");
        uriBuilder.appendQueryParameter("imgSize", "small");
        uriBuilder.appendQueryParameter("num", "1");
        //uriBuilder.appendQueryParameter("alt", "json");

        Log.d(LOG_TAG, uriBuilder.toString());

        url = uriBuilder.toString();

        return url;
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

    private static UpcItem extractUPCFromJson(String upcItemJSON) {
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
            Log.e(LOG_TAG, "Problem parsing the UPC JSON results", e);
        }
        Log.d(LOG_TAG, "item name: " + upcItem.getName());
        Log.d(LOG_TAG, "item #: " + upcItem.getId());
        return upcItem;
    }

    private static byte[] extractThumbnailFromJSON(String thumbnailUrl) {
        if(TextUtils.isEmpty(thumbnailUrl)) {
            return null;
        }

        byte[] thumbnail = null;
        try {
            JSONObject baseJsonResponse = new JSONObject(thumbnailUrl);

            JSONArray imageArray = baseJsonResponse.getJSONArray("items");
            JSONObject imageInfo = imageArray.getJSONObject(0);
            String link = imageInfo.getString("link");
            Log.d(LOG_TAG, "link = " + link);
            InputStream imageSource;

            imageSource = (InputStream) new URL(link).getContent();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int len;
            byte[] chunk = new byte[16384];

            while ((len = imageSource.read(chunk, 0, chunk.length)) != -1) {
                buffer.write(chunk, 0, len);
            }

            buffer.flush();

            thumbnail = buffer.toByteArray();
            //thumbnail = Drawable.createFromStream(imageSource, link);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the thumbnail JSON results", e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return thumbnail;
    }
}


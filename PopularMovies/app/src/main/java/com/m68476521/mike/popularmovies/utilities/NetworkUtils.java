package com.m68476521.mike.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.m68476521.mike.popularmovies.MovieItem;
import com.m68476521.mike.popularmovies.MovieReview;
import com.m68476521.mike.popularmovies.MovieVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is for pull information to the API
 */

public class NetworkUtils {
    private final static String MOVIES_BASE_URL =
            "https://api.themoviedb.org/3/movie/";
    private final static String PARAM_KEY = "api_key";
    //TODO: Replace KEY_PASS by a valid key from themoviedb.org
    private final static String KEY_PASS = "";
    private final static String POPULAR = "popular";
    private final static String VIDEOS = "videos";
    private final static String REVIEW = "reviews";
    private final static String TOP_RATED = "top_rated";

    private final static String TAG = "NetworkUtils";

    public static URL buildUrl(String type, String id) {
        Uri builtUri = null;
        switch (type) {
            case "popular":
                builtUri = Uri.parse(MOVIES_BASE_URL + POPULAR).buildUpon()
                        .appendQueryParameter(PARAM_KEY, KEY_PASS)
                        .build();
                break;
            case "videos":
                builtUri = Uri.parse(MOVIES_BASE_URL + id + "/" + VIDEOS).buildUpon()
                        .appendQueryParameter(PARAM_KEY, KEY_PASS)
                        .build();
                break;
            case "top":
                builtUri = Uri.parse(MOVIES_BASE_URL + TOP_RATED).buildUpon()
                        .appendQueryParameter(PARAM_KEY, KEY_PASS)
                        .build();
                break;
            case "review":
                builtUri = Uri.parse(MOVIES_BASE_URL + id + "/" + REVIEW).buildUpon()
                        .appendQueryParameter(PARAM_KEY, KEY_PASS)
                        .build();
                break;
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private byte[] getResponseFromHttpUrl(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = urlConnection.getInputStream();

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(urlConnection.getResponseMessage() +
                        ": with" + url.toString());
            }

            int bytesRead;
            byte[] buffer = new byte[1024];

            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();

            return out.toByteArray();
        } finally {
            urlConnection.disconnect();
        }
    }

    public ArrayList<MovieItem> getPopularMovies() {
        String url = buildUrl("popular", "").toString();
        return downloadGalleryItems(url);
    }

    public ArrayList<MovieItem> getTopMovies() {
        String url = buildUrl("top", "").toString();
        return downloadGalleryItems(url);
    }

    public List<MovieVideo> getPopularVideos(String id) {
        String url = buildUrl("videos", id).toString();
        return downloadVideos(url);
    }

    public MovieReview getVideoReview(String id) {
        String url = buildUrl("review", id).toString();
        return downloadReview(url);
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getResponseFromHttpUrl(urlSpec));
    }

    private ArrayList<MovieItem> downloadGalleryItems(String url) {
        ArrayList<MovieItem> items = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            Log.d(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.i(TAG, "Failed to fetch items" + ioe);
        }
        return items;
    }

    private List<MovieVideo> downloadVideos(String url) {
        List<MovieVideo> items = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            Log.d(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItemVideos(items, jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.i(TAG, "Failed to fetch items" + ioe);
        }
        return items;
    }

    private MovieReview downloadReview(String url) {
        MovieReview myReview = new MovieReview();
        try {
            String jsonString = getUrlString(url);
            Log.d(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseVideoReview(myReview, jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.i(TAG, "Failed to fetch items" + ioe);
        }
        return myReview;
    }

    private void parseItems(List<MovieItem> items, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONArray questionsJsonObject = jsonBody.getJSONArray("results");

        for (int i = 0; i < questionsJsonObject.length(); i++) {
            JSONObject questionJsonObject = questionsJsonObject.getJSONObject(i);

            MovieItem item = new MovieItem();
            item.setVoteCount(questionJsonObject.getString("video"));
            item.setId(questionJsonObject.getString("id"));
            item.setVideo(questionJsonObject.getBoolean("video"));
            item.setVoteAverage(questionJsonObject.getDouble("vote_average"));
            item.setTitle(questionJsonObject.getString("title"));
            item.setPoster(questionJsonObject.getString("poster_path"));
            item.setOriginalLanguage(questionJsonObject.getString("original_language"));
            item.setOriginalTitle(questionJsonObject.getString("original_title"));
            item.setBackPoster(questionJsonObject.getString("backdrop_path"));
            item.setAdult(questionJsonObject.getBoolean("adult"));
            item.setDescription(questionJsonObject.getString("overview"));
            item.setReleaseDate(questionJsonObject.getString("release_date"));
            Log.d(TAG, questionJsonObject.getString("title"));
            items.add(item);
        }
    }

    private void parseItemVideos(List<MovieVideo> items, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONArray questionsJsonObject = jsonBody.getJSONArray("results");
        for (int i = 0; i < questionsJsonObject.length(); i++) {
            JSONObject questionJsonObject = questionsJsonObject.getJSONObject(i);

            MovieVideo item = new MovieVideo();
            item.setIso1(questionJsonObject.getString("iso_639_1"));
            item.setId(questionJsonObject.getString("id"));
            item.setIso2(questionJsonObject.getString("iso_3166_1"));
            item.setKey(questionJsonObject.getString("key"));
            item.setName(questionJsonObject.getString("name"));
            item.setSite(questionJsonObject.getString("site"));
            item.setSize(questionJsonObject.getString("size"));
            item.setType(questionJsonObject.getString("type"));
            Log.d(TAG, questionJsonObject.getString("name"));
            items.add(item);
        }
    }

    private void parseVideoReview(MovieReview myReview, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONArray questionsJsonObject = jsonBody.getJSONArray("results");

        for (int i = 0; i < questionsJsonObject.length(); i++) {
            JSONObject questionJsonObject = questionsJsonObject.getJSONObject(i);
            myReview.setAuthor(questionJsonObject.getString("author"));
            myReview.setReview(questionJsonObject.getString("content"));
            myReview.setId(questionJsonObject.getString("id"));
            Log.d(TAG, questionJsonObject.getString("author"));
        }
    }
}

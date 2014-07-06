package br.com.login;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Fernando on 05/07/2014.
 */
public class Json {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public static void post(PostListener postListener, String url, List<NameValuePair> params) {
        new PostAsync(postListener, url, params).execute();
    }

    public interface PostListener {
        public void onPostSent(JSONObject jsonObject);
    }

    private static class PostAsync extends AsyncTask<Void, Void, Void> {
        private InputStream is = null;
        private JSONObject jObj = null;
        private String json = "";
        private final String url;
        private final List<NameValuePair> params;

        private final PostListener postListener;

        public PostAsync(PostListener postListener, String url, List<NameValuePair> params) {
            this.postListener = postListener;
            this.url = url;
            this.params = params;
        }

        @Override
        protected Void doInBackground(Void... args) {
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append(System.getProperty("line.separator"));
                }
                is.close();
                json = sb.toString();
                Log.e("JSON", json);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // return JSON String
            postListener.onPostSent(jObj);
        }
    }


}

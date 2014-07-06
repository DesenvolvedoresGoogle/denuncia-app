package br.com.denuncia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.br.com.utils.Constants;
import br.com.login.Json;
import br.com.login.R;
import br.com.maps.GPSTracker;

public class SendReportActivity extends Activity implements View.OnClickListener, Json.PostListener {

    private static final String MAPS_API_URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
    private Uri fileUri;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

        fileUri = (Uri) extras.getParcelable("file_uri");

        BitmapDescriptor image;
        BitmapFactory.Options options = new BitmapFactory.Options();

        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                options);

        ((ImageView) findViewById(R.id.send_photo)).setImageBitmap(bitmap);
        findViewById(R.id.send_button).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:

                GPSTracker gps = new GPSTracker(this);
                Location location = new Location(gps.getLocation());

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Json.post(this, MAPS_API_URL + String.valueOf(latitude) + "," + String.valueOf(longitude) + "&sensor=true_or_false", null);

                break;
        }
    }


    @Override
    public void onPostSent(JSONObject jsonObject) {
        try {

            JSONArray results = jsonObject.getJSONArray("results");
            JSONObject address = (JSONObject) results.get(0);
            String formatedAddress = address.getString("formatted_address");

            String title = ((EditText) findViewById(R.id.send_title)).getText().toString();
            String description = ((EditText) findViewById(R.id.send_description)).getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            params.add(new BasicNameValuePair("token", Constants.TOKEN));
            params.add(new BasicNameValuePair("title", title));
            params.add(new BasicNameValuePair("description", description));
            params.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
            params.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
            params.add(new BasicNameValuePair("address", formatedAddress));
            params.add(new BasicNameValuePair("image", fileUri.getPath()));

            Json.PostListener listener = new Json.PostListener() {
                @Override
                public void onPostSent(JSONObject jsonObject) {
                    try {
                        if (jsonObject.getString("status").equals("fail"))
                            Toast.makeText(SendReportActivity.this, jsonObject.getString("erro"), Toast.LENGTH_LONG).show();
                        else {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("report");
                            Intent intent = new Intent(SendReportActivity.this, ReportActivity.class);
                            intent.putExtra("id", jsonObject1.getInt("report_id"));
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            Json.post(listener, Constants.URL + Constants.CREATE_REPORT, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

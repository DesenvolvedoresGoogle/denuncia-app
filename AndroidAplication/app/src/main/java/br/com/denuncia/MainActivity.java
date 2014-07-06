package br.com.denuncia;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.br.com.userController.Control;
import br.com.br.com.utils.Constants;
import br.com.denuncia.adapter.ReportListAdapter;
import br.com.denuncia.model.Report;
import br.com.login.Json;
import br.com.login.LoginActivity;
import br.com.login.R;
import br.com.maps.GPSTracker;

public class MainActivity extends ListActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener, Json.PostListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    private View mHeader;
    private ArrayList<Report> reports;
    private int mActionBarHeight;
    private TypedValue mTypedValue = new TypedValue();
    private int mMinHeaderTranslation;
    private View mPlaceHolderView;
    private TextView mAppTitle;
    private TextView mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        Control mControl = Control.getInstance(this, this, this);

        mHeader = findViewById(R.id.main_header);
        mAppTitle = (TextView) findViewById(R.id.main_app_name);
        mCity = (TextView) findViewById(R.id.main_city);

        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue,
                true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                mTypedValue.data, getResources().getDisplayMetrics());

        mMinHeaderTranslation = -getResources().getDimensionPixelSize(R.dimen.main_header_height) + mActionBarHeight;

        mPlaceHolderView = getLayoutInflater().inflate(
                R.layout.view_placeholder_main, getListView(), false);

        getListView().setOnScrollListener(MainActivity.this);
        getListView().addHeaderView(mPlaceHolderView);
        getListView().smoothScrollToPosition(0);
        getListView().setOnItemClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        int scrollY = getScrollY();
        float translationY = Math.max(-scrollY, mMinHeaderTranslation);
        // translação dos objetos
        mHeader.setTranslationY(translationY);
        mAppTitle.setTranslationY(-translationY + translationY / 5);
        mCity.setTranslationY(-translationY + translationY / 5);
    }

    public int getScrollY() {
        View c = getListView().getChildAt(0);

        //Se a lista estiver vazia
        if (c == null)
            return 0;

        int firstVisiblePosition = getListView().getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1)
            headerHeight = mPlaceHolderView.getHeight();

        //Quando o header ainda não saiu da tela, apenas move a altura do primeiro item
        // menos a distância entre ele e o topo. Quando o header some, move só a sua altura
        return firstVisiblePosition * c.getHeight() - top + headerHeight;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onPostSent(JSONObject jsonObject) {
        reports = new ArrayList<Report>();
        try {
            JSONArray results = jsonObject.getJSONArray("reports");
            for (int i = 0; i < results.length(); i++) {
                JSONObject json = results.getJSONObject(i);
                reports.add(new Report(new URL(Constants.IMAGE + json.getString("photo")),
                        json.getString("title"), json.getString("description"),
                        json.getString("address"), json.getDouble("latitude"), json.getDouble("longitude")));
                Log.v("test", i + "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v("test", -1 + "");
        new ImageLoader().execute();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();

        GPSTracker gps = new GPSTracker(this);
        Location location = new Location(gps.getLocation());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        params.add(new BasicNameValuePair("token", Constants.TOKEN));
        params.add(new BasicNameValuePair("latitude", String.valueOf(location.getLatitude())));
        params.add(new BasicNameValuePair("longitude", String.valueOf(location.getLongitude())));

        Json.post(this, Constants.URL + Constants.NEAR_REPORTS, params);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private class ImageLoader extends AsyncTask<Void, Void, Void> {

        private ArrayList<Pair<Report, Bitmap>> info;

        @Override
        protected Void doInBackground(Void... urls) {

            info = new ArrayList<Pair<Report, Bitmap>>();

            try {
                //TODO Verificar conexão
                for (Report r : reports)
                    info.add(new Pair<Report, Bitmap>(r, BitmapFactory.decodeStream(r.getPhoto()
                            .openConnection().getInputStream())));


            } catch (IOException e) {
                Log.v(TAG, "Erro ao carregar URL");
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(new ReportListAdapter(MainActivity.this, info));
        }
    }
}

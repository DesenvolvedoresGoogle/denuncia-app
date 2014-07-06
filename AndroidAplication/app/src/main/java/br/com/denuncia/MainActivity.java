package br.com.denuncia;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.br.com.utils.Constants;
import br.com.denuncia.adapter.ReportListAdapter;
import br.com.denuncia.model.Report;
import br.com.login.Json;
import br.com.login.R;
import br.com.maps.GPSTracker;

public class MainActivity extends ListActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener, Json.PostListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private View mHeader;
    private ArrayList<Report> reports;
    private int mActionBarHeight;
    private TypedValue mTypedValue = new TypedValue();
    private int mMinHeaderTranslation;
    private View mPlaceHolderView;
    private TextView mAppTitle;
    private TextView mCity;

    private Uri fileUri;

    private static final String IMAGE_DIRECTORY_NAME = "DenunciaApp";

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        mHeader = findViewById(R.id.main_header);
        mAppTitle = (TextView) findViewById(R.id.main_app_name);
        mCity = (TextView) findViewById(R.id.main_city);
        findViewById(R.id.main_footer).setOnClickListener(this);

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
        if(position == 0)
            return;

        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra("id", reports.get(position - 1).getId());
        startActivity(intent);
    }

    @Override
    public void onPostSent(JSONObject jsonObject) {
        reports = new ArrayList<Report>();
        try {
            JSONArray results = jsonObject.getJSONArray("reports");
            for (int i = 0; i < results.length(); i++) {
                JSONObject json = results.getJSONObject(i);
                reports.add(new Report(json.getInt("report_id"), new URL(Constants.IMAGE + json.getString("photo").replace(".jpg", "SMALL.jpg")),
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
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.main_footer:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                // start the image capture Intent
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

                break;
        }
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME
        );

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
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

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(this, SendReportActivity.class);
            intent.putExtra("file_uri", fileUri);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("file_uri", fileUri);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri = savedInstanceState.getParcelable("file_uri");
    }
}

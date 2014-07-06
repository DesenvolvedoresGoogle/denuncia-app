package br.com.denuncia;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.br.com.utils.Constants;
import br.com.denuncia.model.Comment;
import br.com.denuncia.model.Report;
import br.com.login.Json;
import br.com.login.R;
import br.com.maps.MapsActivity;

public class ReportActivity extends ListActivity implements AbsListView.OnScrollListener, Json.PostListener {
    private static final String TAG = "ReportActivity";
    private Report mReport;
    private View mHeader;
    private int mMinHeaderTranslation;
    private int mActionBarHeight;
    private TypedValue mTypedValue = new TypedValue();
    private ArrayList<Comment> comments;
    private View mPlaceHolderView;
    private ImageView mReportImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia);

        Bundle extras = getIntent().getExtras();
        if(extras == null)
            finish();

        int id = extras.getInt("id");

        ///Report
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        params.add(new BasicNameValuePair("token", Constants.TOKEN));
        params.add(new BasicNameValuePair("report_id", String.valueOf(id)));

        Json.post(this, Constants.URL + Constants.SINGLE_REPORT, params);

        mHeader = findViewById(R.id.report_header);
        mReportImage = (ImageView) findViewById(R.id.report_photo);

        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue,
                true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                mTypedValue.data, getResources().getDisplayMetrics());

        mMinHeaderTranslation = -getResources().getDimensionPixelSize(R.dimen.report_header_height) + mActionBarHeight;

        mPlaceHolderView = getLayoutInflater().inflate(
                R.layout.view_placeholder_report, getListView(), false);

        getListView().setOnScrollListener(ReportActivity.this);
        getListView().addHeaderView(mPlaceHolderView);
        getListView().smoothScrollToPosition(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.denuncia, menu);
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
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        int scrollY = getScrollY();
        float translationY = Math.max(-scrollY, mMinHeaderTranslation);
        // translação dos objetos
        mHeader.setTranslationY(translationY);
        mReportImage.setTranslationY(-translationY + translationY / 5);
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

    public void showMap(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("latitude", mReport.getLatitude());
        intent.putExtra("longitude", mReport.getLongitude());
        startActivity(intent);
    }

    @Override
    public void onPostSent(JSONObject jsonObject) {
        try {
            JSONObject json = (JSONObject) jsonObject.get("report");
            mReport = new Report(json.getInt("report_id"), new URL(Constants.IMAGE + json.getString("photo")),
                    json.getString("title"), json.getString("description"),
                    json.getString("address"), json.getDouble("latitude"), json.getDouble("longitude"));

            new ImageLoader().execute();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private class ImageLoader extends AsyncTask<Void, Void, Void> {

        private Bitmap image;

        @Override
        protected Void doInBackground(Void... urls) {

            try {
                //TODO Verificar conexão
                if (mReport.getPhoto() != null)
                    image = BitmapFactory.decodeStream(mReport.getPhoto()
                            .openConnection().getInputStream());


            } catch (IOException e) {
                Log.v(TAG, "Erro ao carregar URL");
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(Void result) {

            if (image != null)
                mReportImage.setImageBitmap(image);
        }
    }
}

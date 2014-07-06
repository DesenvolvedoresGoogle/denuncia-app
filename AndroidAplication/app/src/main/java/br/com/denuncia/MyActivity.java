package br.com.denuncia;

import android.annotation.SuppressLint;
import android.app.ListActivity;
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
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import br.com.denuncia.adapter.ReportListAdapter;
import br.com.denuncia.model.Report;
import br.com.login.R;

public class MyActivity extends ListActivity implements AbsListView.OnScrollListener {

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

        reports = new ArrayList<Report>();

        try {
            reports.add(new Report(new URL("http://www.sulmg.com.br/images/stories/android.png"), "Titulo", "Decrição", -21.7600964, -43.3471169));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mHeader = findViewById(R.id.main_header);
        mAppTitle = (TextView) findViewById(R.id.main_app_name);
        mCity = (TextView) findViewById(R.id.main_city);

        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue,
                true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                mTypedValue.data, getResources().getDisplayMetrics());

        mMinHeaderTranslation = -getResources().getDimensionPixelSize(R.dimen.main_header_height) + mActionBarHeight;

        new ImageLoader().execute();

        mPlaceHolderView = getLayoutInflater().inflate(
                R.layout.view_header_placeholder, getListView(), false);

        getListView().setOnScrollListener(MyActivity.this);
        getListView().addHeaderView(mPlaceHolderView);
        getListView().smoothScrollToPosition(0);
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

    private class ImageLoader extends AsyncTask<Void, Void, Void> {

        private ArrayList<Bitmap> images;

        @Override
        protected Void doInBackground(Void... urls) {

            images = new ArrayList<Bitmap>();

            try {
                //TODO Verificar conexão
                for (Report r : reports)
                    images.add(BitmapFactory.decodeStream(r.getPhoto()
                            .openConnection().getInputStream()));


            } catch (IOException e) {
                Log.v(TAG, "Erro ao carregar URL");
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(new ReportListAdapter(MyActivity.this, images));
        }
    }
}

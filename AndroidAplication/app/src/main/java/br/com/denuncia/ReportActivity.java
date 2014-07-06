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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import br.com.denuncia.adapter.CommentListAdapter;
import br.com.denuncia.model.Comment;
import br.com.denuncia.model.Report;
import br.com.login.R;
import br.com.maps.MapsActivity;

public class ReportActivity extends ListActivity implements AbsListView.OnScrollListener {
    private static final String TAG = "ReportActivity";
    private Report mReport;
    private View mHeader;
    private View mBar;
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

        try {
            mReport = new Report(new URL("http://osl.ulpgc.es/wosl/wp-content/uploads/2014/05/new-google-chrome-logo.jpg/"), "Titulo", "Decrição", -21.7600964, -43.3471169);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        comments = new ArrayList<Comment>();

        try {
            for (int i = 0; i < 100; i++)
                comments.add(new Comment("Comentário teste" + i, new URL("http://google.com/")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mBar = findViewById(R.id.report_bar);
        mHeader = findViewById(R.id.report_header);
        mReportImage = (ImageView) findViewById(R.id.report_photo);



        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue,
                true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                mTypedValue.data, getResources().getDisplayMetrics());

        mMinHeaderTranslation = -getResources().getDimensionPixelSize(R.dimen.report_header_height) + mActionBarHeight;

        new ImageLoader().execute();

        mPlaceHolderView = getLayoutInflater().inflate(
                R.layout.view_placeholder_report, getListView(), false);

        setListAdapter(new CommentListAdapter(this, comments));
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
        mBar.setTranslationY(translationY);
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

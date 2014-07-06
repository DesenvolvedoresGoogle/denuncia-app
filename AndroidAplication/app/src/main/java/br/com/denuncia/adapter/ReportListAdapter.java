package br.com.denuncia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import br.com.login.R;

/**
 * Created by User on 05/07/2014.
 */
public class ReportListAdapter extends ArrayAdapter<Bitmap> {
    private ArrayList<Bitmap> bitmaps;

    public ReportListAdapter(Context context, ArrayList<Bitmap> bitmaps) {
        super(context, R.layout.view_report, bitmaps);
        this.bitmaps = bitmaps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if(row == null){
            row = LayoutInflater.from(getContext()).inflate(R.layout.view_report, parent, false);
            holder = new Holder();

            holder.photo = ((ImageView) row.findViewById(R.id.view_report_photo));

            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }

        holder.photo.setImageBitmap(bitmaps.get(position));

        return row;
    }

    private class Holder{
        ImageView photo;
    }
}

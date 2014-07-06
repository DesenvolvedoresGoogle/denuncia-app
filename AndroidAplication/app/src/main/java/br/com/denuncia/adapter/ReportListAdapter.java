package br.com.denuncia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.denuncia.model.Report;
import br.com.login.R;

/**
 * Created by User on 05/07/2014.
 */
public class ReportListAdapter extends ArrayAdapter<Pair<Report, Bitmap>> {
    private ArrayList<Pair<Report, Bitmap>> info;

    public ReportListAdapter(Context context, ArrayList<Pair<Report, Bitmap>> info) {
        super(context, R.layout.view_report, info);
        this.info = info;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if(row == null){
            row = LayoutInflater.from(getContext()).inflate(R.layout.view_report, parent, false);
            holder = new Holder();

            holder.photo = ((ImageView) row.findViewById(R.id.view_report_photo));
            holder.title = ((TextView) row.findViewById(R.id.view_report_title));
            holder.address = ((TextView) row.findViewById(R.id.view_report_address));

            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }

        holder.photo.setImageBitmap(info.get(position).second);
        holder.title.setText(info.get(position).first.getTitle());
        holder.address.setText(info.get(position).first.getAddress());

        return row;
    }

    private class Holder{
        ImageView photo;
        TextView title, address;
    }
}

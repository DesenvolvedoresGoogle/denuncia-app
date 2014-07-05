package br.com.denuncia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.denuncia.R;
import br.com.denuncia.model.Comment;

/**
 * Created by User on 05/07/2014.
 */
public class CommentListAdapter extends ArrayAdapter<String> {

    private ArrayList<Comment> comments;

    public CommentListAdapter(Context context, ArrayList<Comment> comments) {
        super(context, R.layout.view_comment);
        this.comments = comments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if(row == null){
            row = LayoutInflater.from(getContext()).inflate(R.layout.view_comment, parent, false);
            holder = new Holder();

            holder.text = ((TextView) row.findViewById(R.id.comment_text));
            //holder.userPhoto = ((ImageView) row.findViewById(R.id.comment_user_photo));

            row.setTag(holder);
        }

        holder.text.setText(comments.get(position).getText());

        return row;
    }

    private class Holder{
        TextView text;
        ImageView userPhoto;
    }
}

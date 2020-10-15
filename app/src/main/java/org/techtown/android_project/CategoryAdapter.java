package org.techtown.android_project;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<DataItem> {

    Context context;
    int layoutResourceId;
    List<DataItem> data = null;

    static class DataHolder
    {
        ImageView categoryimage;
        TextView categorytext;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DataHolder holder = null;

        if(convertView==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();

            convertView = inflater.inflate(layoutResourceId,parent,false);
            holder = new DataHolder();
            holder.categoryimage = (ImageView)convertView.findViewById(R.id.categoryimage);
            holder.categorytext = (TextView)convertView.findViewById(R.id.categorytext);

            convertView.setTag(holder);
        }
        else
        {
            holder = (DataHolder)convertView.getTag();
        }

        DataItem dataItem = data.get(position);
        holder.categorytext.setText(dataItem.categoryname);
        holder.categoryimage.setImageResource(dataItem.readIdThumbnail);

        return convertView;

    }

    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<DataItem> objects) {
        super(context, resource, objects);

        this.layoutResourceId = resource;
        this.context = context;
        this.data = objects;


    }
}

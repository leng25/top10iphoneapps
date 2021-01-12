package com.e.top10iphonesapps;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResources;
    private final LayoutInflater mLayoutInflater;
    private List<FeedEntry> applications;

    public FeedAdapter(Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.layoutResources = resource;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.applications = applications;
    }

    @Override
    public int getCount() {
        return applications.size();
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        ViewHolder viewHolder ;

        if(convertView == null){
            Log.d(TAG, "getView: call with null converted view");
            convertView = mLayoutInflater.inflate(layoutResources, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //replaced by ViewHolder
        //TextView TVname = (TextView)convertView.findViewById(R.id.TVname);
        //TextView TVartist = (TextView)convertView.findViewById(R.id.TVartist);
        //TextView TVsummary = (TextView) convertView.findViewById(R.id.TVsummary);

        FeedEntry currentapp = applications.get(position);

        viewHolder.TVname.setText(currentapp.getName());
        viewHolder.TVartist.setText(currentapp.getArtist());
        viewHolder.TVsummary.setText(currentapp.getSummary());
        return convertView;
    }

    private class ViewHolder{
        final TextView TVname;
        final TextView TVartist;
        final TextView TVsummary;

        ViewHolder(View v){
            this.TVname = (TextView)v.findViewById(R.id.TVname);
            this.TVartist = (TextView)v.findViewById(R.id.TVartist);
            this.TVsummary = (TextView)v.findViewById(R.id.TVsummary);
        }
    }
}

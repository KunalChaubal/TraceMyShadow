package com.kunalc.trackerapp.shadow.adapter;

/**
 * Created by KunalC on 6/10/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kunalc.trackerapp.shadow.R;
import com.kunalc.trackerapp.shadow.activity.ActivityBase;
import com.kunalc.trackerapp.shadow.bean.TrackDetails;

import java.util.ArrayList;
import java.util.List;

public class ListCustomAdapter extends BaseAdapter {
    List<TrackDetails> nameList = new ArrayList<TrackDetails>();
    Context context;
    private static LayoutInflater inflater = null;

    public ListCustomAdapter(ActivityBase mainActivity, List<TrackDetails> prgmNameList) {
        // TODO Auto-generated constructor stub
        nameList = prgmNameList;
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView address;
        TextView time;
        TextView duration;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_layout, null);

        holder.address = (TextView) rowView.findViewById(R.id.last_seen);
        holder.time = (TextView) rowView.findViewById(R.id.time);
        holder.duration = (TextView) rowView.findViewById(R.id.duration);

        for (TrackDetails trackbean : nameList) {
            holder.address.setText(trackbean.getAddress());
            holder.duration.setText((trackbean.getTimeSpent() + ""));
            holder.time.setText(trackbean.getVisitedDate() + " Minutes");
        }

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                TrackDetails trackDetailsBean = nameList.get(position);
                trackDetailsBean.getLatitude();
                trackDetailsBean.getLongitude();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + trackDetailsBean.getLatitude() + ">,<" + trackDetailsBean.getLongitude() + ">?q=<" + trackDetailsBean.getLatitude() + ">,<" + trackDetailsBean.getLongitude() + ">"));
                context.startActivity(intent);
            }
        });
        return rowView;
    }

}

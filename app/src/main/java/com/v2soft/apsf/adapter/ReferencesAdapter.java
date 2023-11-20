package com.v2soft.apsf.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.v2soft.apsf.R;
import com.v2soft.apsf.model.User;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by srikanth.m on 3/1/2018.
 */

public class ReferencesAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<User> newsList;
    private String strAddress = "";

    public ReferencesAdapter(Activity activity, ArrayList<User> newsList) {
        this.activity = activity;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int location) {
        return newsList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListNewsViewHolder holder = null;
        if (convertView == null) {
            holder = new ListNewsViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.ref_list_row, parent, false);
            holder.t = (TextView) convertView.findViewById(R.id.t);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.regid = (TextView) convertView.findViewById(R.id.regid);
            holder.phno = (TextView) convertView.findViewById(R.id.phno);
            holder.email = (TextView) convertView.findViewById(R.id.email);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        } else {
            holder = (ListNewsViewHolder) convertView.getTag();
        }

        holder.name.setId(position);
        holder.regid.setId(position);
        holder.phno.setId(position);
        holder.email.setId(position);

        try {
            holder.t.setText(newsList.get(position).name.substring(0, 1).toUpperCase(Locale.ENGLISH));
            holder.name.setText(newsList.get(position).name.substring(0, 1).toUpperCase(Locale.ENGLISH) + newsList.get(position).name.substring(1));
            holder.regid.setText("RegId : " + newsList.get(position).regid);
            holder.phno.setText("Phone:" + newsList.get(position).phno);
            holder.email.setText("E-Mail:" + newsList.get(position).mailid);

            strAddress = newsList.get(position).address1.trim() + "," + newsList.get(position).address2.trim() + "," + newsList.get(position).pincode.trim();
            if (strAddress != null) {
                if (strAddress.endsWith(","))
                    strAddress = strAddress.substring(0, strAddress.length() - 1);
                if (strAddress.startsWith(","))
                    strAddress = strAddress.substring(1);
            }
            holder.address.setText(strAddress);

            strAddress = newsList.get(position).status.substring(0, 1).toUpperCase(Locale.ENGLISH) + newsList.get(position).status.substring(1).toLowerCase(Locale.ENGLISH);
            holder.status.setText(strAddress);
            if (strAddress.startsWith("Veri"))
                holder.status.setTextColor(activity.getResources().getColor(R.color.green));
            else
                holder.status.setTextColor(activity.getResources().getColor(R.color.colorAccent));

        } catch (Exception e) {
        }
        return convertView;
    }

    class ListNewsViewHolder {
        TextView t, name, regid, phno, email, address, status;
    }
}
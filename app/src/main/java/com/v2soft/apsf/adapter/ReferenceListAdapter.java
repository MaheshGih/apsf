package com.v2soft.apsf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2soft.apsf.R;
import com.v2soft.apsf.model.User;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by srikanth.m on 8/5/2018.
 */

public class ReferenceListAdapter extends RecyclerView.Adapter<ReferenceListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<User> albumList;
    private ArrayList<User> mainList = null;
    private ArrayList<User> arrayList;
    private String strAddress = "";

    public ReferenceListAdapter(Context mContext, ArrayList<User> albumLst) {
        this.mContext = mContext;
        this.albumList = albumLst;
        this.mainList = albumLst;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(mainList);
    }

    public void setData(ArrayList<User> albumLst) {
        this.albumList = albumLst;
        this.mainList = albumLst;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(mainList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count, location, phno, emailId;
        public ImageView thumbnail;
        public TextView book;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            book = (TextView) view.findViewById(R.id.tv_book);
            location = (TextView) view.findViewById(R.id.location);
            phno = (TextView) view.findViewById(R.id.phno);
            emailId = (TextView) view.findViewById(R.id.emailId);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ref_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.title.setText("Name: " + albumList.get(position).name.substring(0, 1).toUpperCase(Locale.ENGLISH) + albumList.get(position).name.substring(1));
        holder.count.setText("Phone : " + albumList.get(position).phno);
        holder.location.setText("Reg id : " + albumList.get(position).regid);
        holder.phno.setText(albumList.get(position).status.substring(0, 1).toUpperCase(Locale.ENGLISH) + albumList.get(position).status.substring(1).toLowerCase(Locale.ENGLISH));
        if (albumList.get(position).status.trim().isEmpty())
            holder.phno.setVisibility(View.GONE);

        strAddress = albumList.get(position).address1.trim() + "," + albumList.get(position).address2.trim() + "," + albumList.get(position).pincode.trim();
        if (strAddress != null) {
            if (strAddress.endsWith(","))
                strAddress = strAddress.substring(0, strAddress.length() - 1);
            if (strAddress.length() > 1 && strAddress.startsWith(","))
                strAddress = strAddress.substring(1);
            if (strAddress.length() == 1 && strAddress.startsWith(","))
                strAddress = "";
        }
        holder.emailId.setText("Address : " + strAddress);

        if (albumList.get(position).status.toLowerCase(Locale.ENGLISH).startsWith("veri"))
            holder.phno.setTextColor(mContext.getResources().getColor(R.color.green));
        else
            holder.phno.setTextColor(mContext.getResources().getColor(R.color.colorAccent));

        holder.thumbnail.setBackgroundResource(R.drawable.avathar);

        // loading album cover using Glide library
        //Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        /*if (!album.getType().equalsIgnoreCase("VIP"))
            holder.book.setVisibility(View.GONE);
        else
            holder.book.setVisibility(View.VISIBLE);*/

        holder.book.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void filter(String charText, int nCase) {
        charText = charText.toLowerCase(Locale.getDefault());
        mainList.clear();
        if (charText.length() == 0) {
            mainList.addAll(arrayList);
        } else {
            for (User vip : arrayList) {
                if (nCase == 0) {
                    if (vip.name.toLowerCase(Locale.ENGLISH)
                            .contains(charText)) {
                        mainList.add(vip);
                    }
                } else {
                    if (vip.phno.toLowerCase(Locale.ENGLISH)
                            .contains(charText)) {
                        mainList.add(vip);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
package com.example.l.netdisk;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class homeAdapter extends RecyclerView.Adapter<homeAdapter.ViewHolder> {
    private ArrayList<Bangumi> bangumiList;
    private Context context;

    public homeAdapter(ArrayList<Bangumi> bangumiList){
        this.bangumiList = bangumiList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView textView;

        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.bangumi_img);
            textView = view.findViewById(R.id.bangumi_name);
            cardView = (CardView) view;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view =  LayoutInflater.from(context).inflate(R.layout.bangumi_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Bangumi bangumi = bangumiList.get(position);
                Intent intent = new Intent(context,BangumiDetail.class);
                intent.putExtra("Title",bangumi.getName());
                intent.putExtra("Url",bangumi.getDetailUrl());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bangumi bangumi = bangumiList.get(position);
        holder.textView.setText(bangumi.getName());
        Glide.with(context).load(bangumi.getPicPath()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return bangumiList.size();
    }
}

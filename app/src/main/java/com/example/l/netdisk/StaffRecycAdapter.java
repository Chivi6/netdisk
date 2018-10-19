package com.example.l.netdisk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class StaffRecycAdapter extends RecyclerView.Adapter <StaffRecycAdapter.ViewHolder>{
    private List<Cast> casts;
    private Context mContext;
    public StaffRecycAdapter(List<Cast> casts){
        this.casts = casts;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.charater);
            textView = view.findViewById(R.id.cast_detail);
        }
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.staff_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cast cast = casts.get(position);
        Glide.with(mContext).load(cast.getCastPicPath()).into(holder.imageView);
        holder.textView.setText(cast.getCastName());
    }
}

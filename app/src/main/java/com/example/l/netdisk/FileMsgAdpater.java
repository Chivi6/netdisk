package com.example.l.netdisk;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FileMsgAdpater extends RecyclerView.Adapter <FileMsgAdpater.ViewHolder>{
    ArrayList<FileMsg> filelist;

    public FileMsgAdpater(ArrayList<FileMsg> filelist) {
        this.filelist = filelist;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView fileName;
        TextView DLStatus;
        TextView DLSpeed;

        public ViewHolder(View view){
            super(view);
            this.view = view;
            fileName = view.findViewById(R.id.filename);
            DLSpeed = view.findViewById(R.id.DL_speed);
            DLStatus = view.findViewById(R.id.DL_status);
        }
    }

    @Override
    public int getItemCount() {
        return filelist.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filelist_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    if (!filelist.get(position).getParent().equals("NO"))
                    {
                        new Filelist().execute(new String[]{filelist.get(position).getParent(), filelist.get(position).getFilename()});
                    }
                }
            });
            return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileMsg fileMsg = filelist.get(position);
        holder.fileName.setText(fileMsg.getFilename());
        holder.DLStatus.setText(fileMsg.getFilestatus());
        holder.DLSpeed.setText(fileMsg.getDLspeed());
    }
}

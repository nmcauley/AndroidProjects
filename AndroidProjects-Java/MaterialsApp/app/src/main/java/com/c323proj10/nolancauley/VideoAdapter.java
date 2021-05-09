package com.c323proj10.nolancauley;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    Context context;
    ArrayList<Video> videoList;

    public VideoAdapter(Context ct, ArrayList<Video> vl) {
        context = ct;
        videoList = vl;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_videolist, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //sets all values depending on the following
        holder.videoName.setText(videoList.get(position).title);
        String length = String.valueOf(videoList.get(position).duration);
        holder.videoDuration.setText(length);
        holder.favoriteToggle.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.grey_star));
        holder.favoriteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    holder.favoriteToggle.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.gold_star));
                }
                else
                    holder.favoriteToggle.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.grey_star));
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView videoName, videoDuration;
        //favourite icon
        ToggleButton favoriteToggle;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            videoName = itemView.findViewById(R.id.videoTitle);
            videoDuration = itemView.findViewById(R.id.videoDuration);
            favoriteToggle = itemView.findViewById(R.id.favoriteVideoToggle);
        }
    }
}

package com.c323proj10.nolancauley;

import android.content.Context;
import android.content.DialogInterface;
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

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

    Context context;
    ArrayList<Song> songList;

    public SongAdapter(Context ct, ArrayList<Song> sl) {
        context = ct;
        songList = sl;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_songlist, parent, false);
        MyOnClickListener listener = new MyOnClickListener();
        view.setOnClickListener(listener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            //obtain our current position

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //sets all values depending on the following
        holder.songName.setText(songList.get(position).title);
        holder.songArtist.setText(songList.get(position).artist);
        holder.songAlbum.setText(songList.get(position).album);
        holder.songDuration.setText(songList.get(position).duration);
        boolean currentFav = songList.get(position).fav;
        if(currentFav == true){
            holder.favoriteToggle.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.gold_star));
        }else{
            holder.favoriteToggle.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.grey_star));
        }
        holder.favoriteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && currentFav == false) {
                    songList.get(position).setFav(true);
                    holder.favoriteToggle.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.gold_star));
                } else {
                    songList.get(position).setFav(false);
                    holder.favoriteToggle.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.grey_star));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView songName, songArtist, songAlbum, songDuration;
        //favourite icon
        ToggleButton favoriteToggle;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            songName = itemView.findViewById(R.id.songName);
            songArtist = itemView.findViewById(R.id.songArtist);
            songAlbum = itemView.findViewById(R.id.songAlbum);
            songDuration = itemView.findViewById(R.id.songDuration);
            favoriteToggle = itemView.findViewById(R.id.favoriteSongToggle);
        }
    }
}

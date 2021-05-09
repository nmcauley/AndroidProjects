package com.c323proj10.nolancauley;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class SongFragment extends Fragment {
    View view;
    ArrayList<Song> songList = new ArrayList();

    //adapter and recycler view
    SongAdapter listAdapter;
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song_layout,container,false);
        recyclerView = view.findViewById(R.id.songRecyclerView);
        obtainMusicMedia();
        //send list through adapter for recycler view
        listAdapter = new SongAdapter(view.getContext(), songList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(listAdapter);
        return view;
    }

    //use content resolver to obtain music files from the MediaStore
    private void obtainMusicMedia() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //have to use Fragment's context
        ContentResolver cr = view.getContext().getContentResolver();
        //obtain data
        //siphon to only music on the device; not ringtones, alarms, etc...
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] columns = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
        };
        //cursor to iterate results
        Cursor cursor = cr.query(uri, columns, selection, null, null);

        if(cursor == null){
            Log.i("SONG_CURSOR", "is null");
            return;
        }
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            do{
                //create each Song object and then place into songList ArrayList
                //read your information here
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
//                Log.i("id", id);
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                Log.i("TITLE", title);
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//                Log.i("TITLE", title);
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//                Log.i("ALBUM", album);
                String length = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

//                Log.i("DURATION", length);

                Song song = new Song(id, title, artist, album, length, path);
                //add to list to be displayed
                songList.add(song);
            }while(cursor.moveToNext());
        }
        if (songList.isEmpty()){
            Toast.makeText(getContext(), "No Songs on device to display", Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }


}

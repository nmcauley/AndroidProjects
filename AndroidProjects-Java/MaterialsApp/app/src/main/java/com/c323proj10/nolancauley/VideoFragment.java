package com.c323proj10.nolancauley;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;

public class VideoFragment extends Fragment {
    View view;
    ArrayList<Video> videoList = new ArrayList();

    //adapter and recycler view
    VideoAdapter listAdapter;
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_layout,container,false);

        recyclerView = view.findViewById(R.id.videoRecyclerView);
        obtainVideoMedia();
        //send list through adapter for recycler view
        listAdapter = new VideoAdapter(view.getContext(), videoList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(listAdapter);

        return view;
    }

    private void obtainVideoMedia() {
        ContentResolver contentResolver = view.getContext().getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                Video videoModel  = new Video(title, Long.parseLong(duration), Uri.parse(data));
                videoList.add(videoModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.i("SIZE", String.valueOf(videoList.size()));
        if(videoList.isEmpty()){
            Toast.makeText(getContext(), "No Videos on device to display", Toast.LENGTH_LONG).show();
        }
    }
}

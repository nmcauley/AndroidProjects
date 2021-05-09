package com.c323proj10.nolancauley;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

public class MusicService extends Service {
    MediaPlayer player;
    ArrayList<Song> songList;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //where player is instantiated
    @Override
    public void onCreate() {
        super.onCreate();

        //register receiver with our action code
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("CLOSE_PLAYER"));

        player = MediaPlayer.create(this, R.raw.minecraft);
        player.setLooping(true);
        Log.i("PLAYER", "playing");
        //volume all the way baby
        player.setVolume(100, 100);
        player.start();
    }


    //private receiver listen if the app is put into the background
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("STATUS").equals("CLOSE")) {
                player.stop();
                //stop the service
                context.stopService(new Intent(context, MusicService.class));
                Log.i("PLAYER", "stopping");
            } else {
                player.setLooping(true);
                Log.i("PLAYER", "playing");
                //volume all the way baby
                player.setVolume(100, 100);
                player.start();
            }
        }
    };
}

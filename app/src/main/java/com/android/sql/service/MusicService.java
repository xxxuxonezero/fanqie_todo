package com.android.sql.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.android.sql.activity.R;

public class MusicService extends Service {
    private MediaPlayer player;
    public static boolean isplay;
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        player=MediaPlayer.create(this, R.raw.first);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!player.isPlaying()){
            player.start();
            isplay=player.isPlaying();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        player.stop();
        isplay=player.isPlaying();
        player.release();
        super.onDestroy();
    }

}

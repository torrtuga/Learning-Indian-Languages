package com.arqamahmad.languageslearnandtalk;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by B on 8/30/2016.
 */
public class Player {

  MediaPlayer mediaPlayer = new MediaPlayer();
    public static Player player;
    String url = "";

    public Player(){
        this.player = this;
    }

    public void playStream (String url){
        if(mediaPlayer != null){
            try{
                mediaPlayer.stop();
            }catch (Exception e){

            }
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();// Starting only when it is prepared
                }
            });
            mediaPlayer.prepareAsync();//Doing in the background thread
        } catch (IOException e) {

        }
        mediaPlayer.start();
    }

}

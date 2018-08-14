package com.example.hp.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<Long> arrayList;
    ArrayList<String> songList;
    public int currentSongIndex = -1;

    List listView;
    MediaPlayer mediaPlayer;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arrayList = new ArrayList<>();
        songList = new ArrayList<>();

        String thisTitle;
        long thisId;

        ContentResolver contentResolver = getContentResolver();
        Uri myUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final Cursor cursor = contentResolver.query(myUri, null, null, null, null);
        if (cursor == null) {

        } else if (!cursor.moveToFirst()) {

        } else {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            do {
                thisId = cursor.getLong(idColumn);
                arrayList.add(thisId);
                thisTitle = cursor.getString(titleColumn);
                songList.add(thisTitle);
            } while (cursor.moveToNext());

        }


        final ImageButton play = findViewById(R.id.play);
        final ImageButton pause = findViewById(R.id.pause);
        final ImageButton next = findViewById(R.id.next);
        final ImageButton previous = findViewById(R.id.prev);
        final ImageButton songs = findViewById(R.id.songList);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentSongIndex == -1) {
                    currentSongIndex++;
                    playSong(currentSongIndex);
                }else if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            }

        });

//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mediaPlayer.pause();
//            }
//        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSongIndex++;
                if (currentSongIndex >= arrayList.size()) {
                    currentSongIndex = 0;
                }
                playSong(currentSongIndex);
            }
        });


        songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,
                        SongList.class);
                startActivity(intent);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSongIndex = currentSongIndex - 1;
                if (currentSongIndex >= arrayList.size()) {
                    currentSongIndex = 0;
                }
                playSong(currentSongIndex);
            }
        });


    }

    public void playSong(int index) {
        long songId = arrayList.get(index);
        if (mediaPlayer != null) {

            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        } else {

        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        final Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), contentUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



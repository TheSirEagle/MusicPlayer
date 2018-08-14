package com.example.hp.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<Long> arrayList;
    ArrayList songList;
    public int currentSongIndex = -1;

    List listView;
    MediaPlayer mediaPlayer;
    ArrayAdapter<String> adapter;
    private boolean isMovingseekBar = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arrayList = new ArrayList<>();
        songList = new ArrayList<>();

        String thisTitle = null;
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
        final ImageButton next = findViewById(R.id.next);
        final ImageButton previous = findViewById(R.id.prev);
        final TextView currentSongName = findViewById(R.id.name);
        final Spinner songListSpinner = findViewById(R.id.spinner);
//        final ImageButton open = findViewById(R.id.openSpinner);



        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,songList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        songListSpinner.setAdapter(adapter);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentSongIndex == -1) {
                    currentSongIndex++;
                    playSong(currentSongIndex);
                } else if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            }

        });


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



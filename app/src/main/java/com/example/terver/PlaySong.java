package com.example.terver;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlaySong extends AppCompatActivity implements ActionPlaying {
    private TextView textView;
    private ImageView play, previous, next;
    public static ArrayList<File> songs = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private String textContent;
    private int position;
    private SeekBar seekBar;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent1 = new Intent(PlaySong.this, MusicService.class);
//        bindService(intent1, this, BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
//        unbindService(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView = findViewById(R.id.textView);
        previous = findViewById(R.id.previous);
        seekBar = findViewById(R.id.seekBar);
        next = findViewById(R.id.next);
        play = findViewById(R.id.play);
        player();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextSong();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPause();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousSong();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                nextSong();
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                } catch (Exception e) {
                }
            }
        }, 0, 1000);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

    }

    @Override
    public void nextSong() {
        mediaPlayer.stop();
        mediaPlayer.release();
        position = ((position + 1) % songs.size());
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextSong();
            }
        });

        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        textContent = songs.get(position).getName().toString();
        textView.setText(textContent);
        play.setImageResource(R.drawable.ic_pause);

    }

    @Override
    public void previousSong() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (position != 0) {
            position = position - 1;
        } else {
            position = songs.size() - 1;
        }
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextSong();
            }
        });
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        textContent = songs.get(position).getName().toString();
        textView.setText(textContent);
        play.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void playPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play.setImageResource(R.drawable.ic_play);

        } else {
            mediaPlayer.start();

            play.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    public void player() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("SongList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
    }


}
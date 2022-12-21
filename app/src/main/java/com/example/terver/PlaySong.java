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

import com.example.terver.databinding.ActivityPlaySongBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlaySong extends AppCompatActivity implements ActionPlaying {
    private ActivityPlaySongBinding binding;
    public static ArrayList<File> songs = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private String textContent;
    private int position;



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        player();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextSong();
            }
        });

        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPause();
            }
        });
        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousSong();
            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                nextSong();
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (mediaPlayer != null) {
                    try {
                        binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    } catch (Exception ignored) {
                    }
                }
            }
        }, 0, 1000);

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        binding.seekBar.setMax(mediaPlayer.getDuration());
        textContent = songs.get(position).getName().toString();
        binding.textView.setText(textContent);
        binding.play.setImageResource(R.drawable.ic_pause);

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
        binding.seekBar.setMax(mediaPlayer.getDuration());
        textContent = songs.get(position).getName().toString();
        binding.textView.setText(textContent);
        binding.play.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void playPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            binding.play.setImageResource(R.drawable.ic_play);

        } else {
            mediaPlayer.start();

            binding.play.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    public void player() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("SongList");
        textContent = intent.getStringExtra("currentSong");
        binding.textView.setText(textContent);
        binding.textView.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        binding.seekBar.setMax(mediaPlayer.getDuration());
    }


}
package com.example.rajmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaySong extends AppCompatActivity {
    TextView textView;
    ImageView play,next,previous;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    int position;
    String str;
    Thread updateSeek;
    Button button;
    Map<Integer,Integer> mp=new HashMap<>();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_song);
        textView=findViewById(R.id.textView);
        play=findViewById(R.id.play);
        previous=findViewById(R.id.previous);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);
        button=findViewById(R.id.button);
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList)bundle.getParcelableArrayList("mysongs");
        str=intent.getStringExtra("currsong");
        textView.setText(str);
        textView.setSelected(true);
        position=intent.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                 mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mp.put(position,mediaPlayer.getCurrentPosition());
                 Toast.makeText(PlaySong.this, "start position has been set.", Toast.LENGTH_SHORT).show();
             }
         });

        updateSeek=new Thread(){
            @Override
            public void run() {
                int currpos=0;
                try{
                    if(mp.containsKey(position))
                    {
                        currpos=mp.get(position);
                        seekBar.setProgress(currpos);
                    }
                    while(currpos<mediaPlayer.getDuration()) {
                        currpos = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currpos);
                        sleep(100);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        updateSeek.start();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else
                {
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if(position!=0)
                {
                    position=position-1;
                }
                else
                {
                    position=songs.size()-1;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                updateSeek=new Thread(){
                    @Override
                    public void run() {
                        int currpos=0;
                        try{
                            if(mp.containsKey(position))
                            {
                                currpos=mp.get(position);
                                seekBar.setProgress(currpos);
                            }
                            while(currpos<mediaPlayer.getDuration()) {
                                currpos = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(currpos);
                                sleep(100);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                };

                updateSeek.start();
                str=songs.get(position).getName().toString();
                textView.setText(str);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if(position!=songs.size()-1)
                {
                    position=position+1;
                }
                else
                {
                    position=0;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                updateSeek=new Thread(){
                    @Override
                    public void run() {
                        int currpos=0;
                        try{
                            if(mp.containsKey(position))
                            {
                                currpos=mp.get(position);
                                seekBar.setProgress(currpos);
                            }
                            while(currpos<mediaPlayer.getDuration()) {
                                currpos = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(currpos);
                                sleep(100);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                };

                updateSeek.start();
                seekBar.setMax(mediaPlayer.getDuration());
                str=songs.get(position).getName().toString();
                textView.setText(str);
            }
        });


    }
}
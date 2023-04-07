package com.loliwe.week4ws;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class RestActivity extends AppCompatActivity {

    TextView timerDisplay;
    Button backBtn, resetBtn;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    boolean restStarted = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        timerDisplay = findViewById(R.id.TxtView3);
        backBtn = findViewById(R.id.Btn01);
        resetBtn = findViewById(R.id.Btn02);

        timer= new Timer();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(restStarted == false) {
                    restStarted = true;
                    backBtn.setText("BACK");
                    backBtn.setTextColor(ContextCompat.getColor(RestActivity.this, R.color.grey));
                    startTimer();
                }
                else {
                    restStarted = false;
                    backBtn.setText("START");

                    //timerTask.cancel();
                    Intent newIntent = new Intent(RestActivity.this, MainActivity.class);
                    startActivity(newIntent);
                }
            }
        });


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerTask != null) {
                    backBtn.setText("START");
                    timerTask.cancel();
                    time = 0.0;
                    restStarted = false;
                    timerDisplay.setText(formatTime(0,0,0));
                }
            }
        });
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                RestActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        timerDisplay.setText(getTimerText());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0, 1000);
    }

    private String getTimerText() {
        int rounded = (int) Math.round(time);

        int hours = (int) ((rounded % 86400) / 3600);
        int minutes = (int) ((rounded % 86400) % 3600) / 60;
        int seconds = (int) ((rounded % 86400) % 3600) % 60;

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
}
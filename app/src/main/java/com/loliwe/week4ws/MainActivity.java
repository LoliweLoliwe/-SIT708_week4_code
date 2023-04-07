package com.loliwe.week4ws;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView countDown, selectedTime1, selectedTime2, selectedTime3, selectedTime4, selectedTime5;
    int select1, select2, select3, i;
    long currentProgress = 0;
    NumberPicker numberPicker1, numberPicker2, numberPicker3;
    Button startBtn, stopBtn;
    ProgressBar progressBar;
    Handler handler = new Handler();
    CountDownTimer countDownTimer;
    long timeLeftInMilliSeconds, timexxx;
    boolean timerRunning;
    MediaPlayer mySong;
    SharedPreferences sharedPref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedTime1 = findViewById(R.id.timerC1);
        selectedTime4 = findViewById(R.id.timerC2);
        selectedTime2 = findViewById(R.id.timerC3);
        selectedTime5 = findViewById(R.id.timerC4);
        selectedTime3 = findViewById(R.id.timerC5);

        progressBar = findViewById(R.id.progressB);
        progressBar.setVisibility(View.INVISIBLE);

        mySong = MediaPlayer.create(MainActivity.this, R.raw.umbayimbayi);

        countDown = findViewById(R.id.counter);
        startBtn = findViewById(R.id.btn1);
        stopBtn = findViewById(R.id.btn2);
        //startBtn.setEnabled(true);

        numberPicker1 =findViewById(R.id.nopicker3);
        numberPicker2 = findViewById(R.id.nopicker2);
        numberPicker3 = findViewById(R.id.nopicker1);

        numberPicker1.setMaxValue(23);
        numberPicker2.setMaxValue(59);
        numberPicker3.setMaxValue(59);
        numberPicker1.setEnabled(false);

        selectedTime1.setText(String.format("%02d", numberPicker1.getValue()));
        selectedTime2.setText(String.format("%02d", numberPicker2.getValue()));
        selectedTime3.setText(String.format("%02d", numberPicker3.getValue()));

        numberPicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Log.i("Checking", "1st NumberPicker");
                selectedTime1.setText(String.format("%02d", newVal));
                //select1 = newVal;
            }
        });

        numberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedTime2.setText(String.format("%02d", newVal));
            }
        });

        numberPicker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedTime3.setText(String.format("%02d", newVal));
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.INVISIBLE);
                startStop();
                Toast.makeText(MainActivity.this, "When the counter has time... You are good!!", Toast.LENGTH_SHORT).show();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.edit().clear().commit();
                mySong.release();
                finish();
            }
        });
    }

    public void startStop() {
        if (timerRunning) {

            mySong.release();
            stopTimer();

        } else {
            resumeStart();
        }
    }

    public void resumeStart() {
        sharedPref = getSharedPreferences("TIME_KEY", MODE_PRIVATE);
        long reShared = sharedPref.getLong("TIMEKEY", 0);

        if(reShared == 0) {
            select2 = Integer.parseInt(String.format("%02d", numberPicker2.getValue()));
            select3 = Integer.parseInt(String.format("%02d", numberPicker3.getValue()));
            long popi = select2 * 60 * 1000 + select3 * 1000;

            if (popi != 0) {
                timeLeftInMilliSeconds = popi;
                //sharedPref.edit().clear().commit();
                startTimer();
                selectedTime1.setText("");
                selectedTime2.setText("");
                selectedTime3.setText("");
                selectedTime4.setText("");
                selectedTime5.setText("");
                startBtn.setTextColor(ContextCompat.getColor(this, R.color.grey));
            }
        } else {
            startBtn.setText("RESUMED");
            timeLeftInMilliSeconds = reShared;
            startTimer();
            selectedTime1.setText("");
            selectedTime2.setText("");
            selectedTime3.setText("");
            selectedTime4.setText("");
            selectedTime5.setText("");
            startBtn.setTextColor(ContextCompat.getColor(this, R.color.grey));
        }
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
            @Override
            public void onTick(long timeLeftInMilliSeconds) {
                //int hours = (int) timeLeftInMilliSeconds / 6000000;
                int minutes = (int) timeLeftInMilliSeconds / 60000;
                int seconds = (int) timeLeftInMilliSeconds % 60000 / 1000;

                String timeLeftText;

                timeLeftText = "" + minutes;
                timeLeftText += ":";

                if (seconds < 10)
                    timeLeftText += "0";
                    timeLeftText += seconds;

                countDown.setText("Time remaining: " + timeLeftText);
                mySong.start();
                timexxx = (long)(minutes * 60000) + (long)(seconds * 1000/60000);
            }

            @Override
            public void onFinish() {

                countDown.setText("Timeout!!");
                timeLeftInMilliSeconds = 0;
                startBtn.setText("START");
                progressBar.setVisibility(View.GONE);
                Restart();
                mySong.release();
                sharedPref.edit().clear().commit();
            }
        }.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i <= 100) {
                    int frr = (int) (timeLeftInMilliSeconds)* 1/100;
                    progressBar.setProgress(i);
                    i ++;
                    handler.postDelayed(this, frr);
                    progressBar.setVisibility(View.VISIBLE);
                }
                else {
                    handler.removeCallbacks(this);
                }
            }
        }, 1);

        startBtn.setText("Pause");
        timerRunning = true;
    }

    public void stopTimer() {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("TIMEKEY", timexxx);
        editor.apply();
        Toast.makeText(MainActivity.this, "Saved time left  " + timexxx, Toast.LENGTH_SHORT).show();

        startBtn.setText("Start");
        timerRunning = false;
        mySong.release();
        progressBar.setVisibility(View.INVISIBLE);
        countDownTimer.cancel();

        Intent newIntent = new Intent(MainActivity.this, RestActivity.class);
        startActivity(newIntent);

    }

    private void Restart() {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle("The count is finished");
        resetAlert.setMessage("Was it funny, exercising??");
        resetAlert.setPositiveButton("RESTART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPref.edit().clear().commit();
                Intent newIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(newIntent);
            }
        });
        resetAlert.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uit();
            }
        });
        sharedPref.edit().clear().commit();
        resetAlert.setCancelable(false);
        resetAlert.show();
        }

    void Uit() {
        sharedPref.edit().clear().commit();
        finish();
    }
}
package zut.roulette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    private final static String TIME_LEFT_KEY = "timeLeft";
    private final static int DISPLAY_TIME = 500;

    private long displayTimeStart;
    private long displayTime = DISPLAY_TIME;
    private final Handler countDownTimer = new Handler();
    private final Runnable nextActivityRunner;
    private boolean isSplashFinished = false;

    public SplashScreenActivity() {
        nextActivityRunner = new Runnable() {
            @Override
            public void run() {
                isSplashFinished = true;
                Intent homeScreenIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(homeScreenIntent);
                SplashScreenActivity.this.finish();
            }
        };
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        if (isSplashFinished) {
            Intent homeScreenIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
            SplashScreenActivity.this.startActivity(homeScreenIntent);
            SplashScreenActivity.this.finish();
        }
        if (savedInstanceState != null) {
            displayTime = savedInstanceState.getLong(TIME_LEFT_KEY);
        }


        displayTimeStart = System.currentTimeMillis();
        countDownTimer.postDelayed(nextActivityRunner, displayTime);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        long elapsedTime = System.currentTimeMillis() - displayTimeStart;
        outState.putLong(TIME_LEFT_KEY, displayTime - elapsedTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.removeCallbacks(nextActivityRunner);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Intent homeScreenIntent = new Intent(this, MainActivity.class);
        this.startActivity(homeScreenIntent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.removeCallbacks(nextActivityRunner);
    }
}

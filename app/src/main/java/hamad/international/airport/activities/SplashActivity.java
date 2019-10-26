package hamad.international.airport.activities;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

import hamad.international.airport.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    private static final int PROGRESS = 0x1;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mProgress = findViewById(R.id.progressBar1);
       // int timeout = 4000; // make the activity visible for 4 seconds
        int timeout = 4000; // make the activity visible for 4 seconds

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                finish();

             /*  Intent homepage = new Intent(MainActivity.this, Nextpage.class);
                startActivity(homepage);*/

               /* Intent homepage = new Intent(MainActivity.this, Login.class);
                startActivity(homepage);*/

                Intent homepage = new Intent(SplashActivity.this, MonitoringActivity.class);
                startActivity(homepage);

            }
        }, timeout);
    }
}

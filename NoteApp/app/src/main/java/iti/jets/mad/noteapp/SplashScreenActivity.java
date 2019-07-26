package iti.jets.mad.noteapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView appTextView;
    private ImageView appImageView;
    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        appTextView=findViewById(R.id.appnameTextView);
        appImageView=findViewById(R.id.splashImageView);
        animation= AnimationUtils.loadAnimation(this,R.anim.splashanim);
        appTextView.startAnimation(animation);
        appImageView.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();

            }
        },3000);
    }
}

package com.example.masbaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView image;
    Animation fade,bottom_animation,blink;
    Handler h=new Handler();
    TextView credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        fade = AnimationUtils.loadAnimation(this,R.anim.fade);
        bottom_animation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        credit = findViewById(R.id.textView2);
        credit.setAnimation(bottom_animation);

        image=findViewById(R.id.logo);
        image.setAnimation(fade);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(MainActivity.this, Home.class);
                startActivity(i);
                finish();
            }
        },5000);

    }
}
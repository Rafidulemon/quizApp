package com.example.masbaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    ImageView image;
    Animation blink;
    Button start_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        blink = AnimationUtils.loadAnimation(this,R.anim.blink);

        image=findViewById(R.id.bulb);
        image.setAnimation(blink);
        start_btn=findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    openQuestion1();
            }
        });
    }

    private void openQuestion1() {
        Intent i = new Intent(this,Question1.class);
        startActivity(i);
    }
    /*public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        networkInfo.isConnected();
        networkInfo.isAvailable();
        if (!networkInfo.isConnected() || !networkInfo.isAvailable()){
            return false;
        }
        return true;
    }*/
}
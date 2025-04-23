package com.example.pbjclicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class MarcoNgai extends AppCompatActivity {

    View pbj;
    TextView add, amount;
    Button upgradeMenu;
    int clickerUp = 1;
    ImageView imageView1, imageView2, imageView3;
    int total, brianUp, stickyFingersUp, sandwichSupremeUp, pbjPerSec = 0;
    boolean music = false;
    RelativeLayout layout;
    MediaPlayer musicPlayer;
    int clickerCost = 10;
    int brianCost = 100;
    int stickyFingersCost = 1000;
    int musicCost = 12345;
    int sandwichSupremeCost = 9999999;
    Handler handler;
    boolean newClicker, newBrian, newSticky, newSandwich = true;
    boolean newMusic = false;


    private BroadcastReceiver passiveIncomeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pbjPerSec = intent.getIntExtra("pbjPerSec", 0);
            total += pbjPerSec;
            amount.setText("PB&Js:\n" + total);
            if (pbjPerSec > 0)
                animateAdd(pbjPerSec);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pbj = findViewById(R.id.pbj);
        add = findViewById(R.id.add);
        amount = findViewById(R.id.amount);
        upgradeMenu = findViewById(R.id.upgradeMenu);
        layout = findViewById(R.id.layout);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        int brian = R.drawable.brian;

        Intent upgrades = getIntent();
        // Upgrade menu -> Main activity
        if (upgrades != null){
            total = upgrades.getIntExtra("PBJ", total);
            clickerUp = upgrades.getIntExtra("clicker", clickerUp);
            brianUp = upgrades.getIntExtra("brianUp", brianUp);
            music = upgrades.getBooleanExtra("music", false);
            stickyFingersUp = upgrades.getIntExtra("stickyFingersUp", stickyFingersUp);
            clickerCost = upgrades.getIntExtra("clickerCost", clickerCost);
            brianCost = upgrades.getIntExtra("brianCost", brianCost);
            stickyFingersCost = upgrades.getIntExtra("stickyFingersCost", stickyFingersCost);
            musicCost = upgrades.getIntExtra("musicCost", musicCost);
            sandwichSupremeCost = upgrades.getIntExtra("sandwichSupremeCost", sandwichSupremeCost);
            newClicker = upgrades.getBooleanExtra("newClicker", newClicker);
            newBrian = upgrades.getBooleanExtra("newBrian", newBrian);
            newSandwich = upgrades.getBooleanExtra("newSandwich", newSandwich);
            newMusic = upgrades.getBooleanExtra("newMusic", newMusic);
            newSticky = upgrades.getBooleanExtra("newSticky", newSticky);
            amount.setText("PB&Js:\n" + total);

            Intent serviceIntent = new Intent(MarcoNgai.this, PassiveIncomeService.class);
            serviceIntent.putExtra("brianUp", brianUp);
            serviceIntent.putExtra("stickyFingersUp", stickyFingersUp);
            serviceIntent.putExtra("music", music);
            startService(serviceIntent);
            music();
        }
        setUpBrian(brian);
        upgradeNotifications();

        // 50% -> 100%
        final ScaleAnimation animation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(400);

        pbj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", String.valueOf(clickerUp));
                total = total + clickerUp;
                pbj.startAnimation(animation);
                amount.setText("PB&Js:\n" + total);
                animateAdd(clickerUp);
            }
        });

        upgradeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicPlayer != null)
                    musicPlayer.stop();
                // Sends current info over to upgrade menu
                Intent intent = new Intent(MarcoNgai.this, Upgrades.class);
                intent.putExtra("PBJ", total);
                intent.putExtra("clicker", clickerUp);
                intent.putExtra("brianUp", brianUp);
                intent.putExtra("music", music);
                intent.putExtra("stickyFingersUp", stickyFingersUp);
                intent.putExtra("clickerCost", clickerCost);
                intent.putExtra("brianCost", brianCost);
                intent.putExtra("stickyFingersCost", stickyFingersCost);
                intent.putExtra("musicCost", musicCost);
                intent.putExtra("sandwichSupremeCost", sandwichSupremeCost);
                intent.putExtra("newClicker", newClicker);
                intent.putExtra("newBrian", newBrian);
                intent.putExtra("newSandwich", newSandwich);
                intent.putExtra("newMusic", newMusic);
                intent.putExtra("newSticky", newSticky);
                startActivity(intent);
                finish();
            }
        });
    }

    private void animateAdd(int value) {
        Random random = new Random();
        int randcol = random.nextInt(10);
        int offsetX = random.nextInt(800);
        int offsetY = random.nextInt(550);

        final TextView add = new TextView(this);
        add.setText("+" + value);
        add.setTextSize(24);
        if (randcol % 2 == 0){
            add.setTextColor(Color.rgb(128,0,128));
        } else {
            add.setTextColor(Color.rgb(191, 150, 116));
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ABOVE, R.id.pbj);
        add.setLayoutParams(params);
        layout.addView(add);

        add.setX(-400 + offsetX);
        add.setY(100 + offsetY);

        add.setVisibility(View.VISIBLE);

        // adds an animation of the +num going up (using translationYBy) slowly (using duration)
        add.animate()
                .translationYBy(-200)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout.removeView(add);
                    }
                })
                .start();
    }

    private void setUpBrian(int brian) {
        if (brianUp >= 1) {
            Glide.with(MarcoNgai.this)
                    .asGif()
                    .load(brian)
                    .into(imageView1);
            imageView1.setVisibility(View.VISIBLE);
        }
        if (brianUp >= 2) {
            Glide.with(MarcoNgai.this)
                    .asGif()
                    .load(brian)
                    .into(imageView2);
            imageView2.setVisibility(View.VISIBLE);
        }
        if (brianUp == 3) {
            Glide.with(MarcoNgai.this)
                    .asGif()
                    .load(brian)
                    .into(imageView3);
            imageView3.setVisibility(View.VISIBLE);
        }
    }
    private void music() {
        if (music) {
            musicPlayer = MediaPlayer.create(MarcoNgai.this, R.raw.peanutbutterjellymusic);
            if (musicPlayer != null) {
                musicPlayer.setLooping(true);
                musicPlayer.start();
            } else {
                Log.e("MainActivity", "Failed to initialize MediaPlayer");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register BroadcastReceiver to receive updates from PassiveIncomeService
        ContextCompat.registerReceiver( this, passiveIncomeReceiver, new IntentFilter("passive_income_update"), ContextCompat.RECEIVER_EXPORTED);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister BroadcastReceiver when activity is paused
        unregisterReceiver(passiveIncomeReceiver);
    }

    private void upgradeNotifications() {
        if (handler != null) // Check if handler already active
            handler.removeCallbacksAndMessages(null);
        else
            handler = new Handler();
        Runnable notificationCheck = new Runnable() {
            @Override
            public void run() {
                if (total >= clickerCost && !newClicker){
                    Toast.makeText(MarcoNgai.this,"NEW UPGRADE: CLICKER!", Toast.LENGTH_SHORT).show();
                    newClicker = true;
                }
                if (total >= brianCost && !newBrian) {
                    Toast.makeText(MarcoNgai.this,"NEW UPGRADE: BRIAN!", Toast.LENGTH_SHORT).show();
                    newBrian = true;
                }
                if (total >= stickyFingersCost && !newSticky) {
                    Toast.makeText(MarcoNgai.this,"NEW UPGRADE: STICKY FINGERS!" , Toast.LENGTH_SHORT).show();
                    newSticky = true;
                }
                if (total >= musicCost && !newMusic){
                    Toast.makeText(MarcoNgai.this, "NEW UPGRADE: THE HOLY MUSIC OF PBJ GODS!", Toast.LENGTH_SHORT).show();
                    newMusic = true;
                }
                if (total >= sandwichSupremeCost && !newSandwich) {
                    Toast.makeText(MarcoNgai.this, "NEW UPGRADE: SANDWICH SUPREME!", Toast.LENGTH_SHORT).show();
                    newSandwich = true;
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(notificationCheck, 1000);
    }
}


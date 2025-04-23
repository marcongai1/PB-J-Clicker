package com.example.pbjclicker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Upgrades extends AppCompatActivity {
    MediaPlayer musicPlayer;
    Button clickerButton, brianButton, musicButton, stickyFingersButton, sandwichSupremeButton, back;
    TextView pbjtotal, clickerdesc, briandesc, musicdesc, stickyFingersdesc, sandwichSupremedesc;
    int clickerUp = 0;
    int total, brianUp, stickyFingersUp, sandwichSupremeUp, pbjPerSec = 0;
    boolean music = false;
    int clickerCost = 10;
    int brianCost = 100;
    int stickyFingersCost = 1000;
    int musicCost = 12345;
    int sandwichSupremeCost = 9999999;
    boolean newClicker, newBrian, newSticky, newSandwich, newMusic = true;


    private BroadcastReceiver passiveIncomeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pbjPerSec = intent.getIntExtra("pbjPerSec", 0);
            total += pbjPerSec;
            pbjtotal.setText("PB&Js: " + total);
            checkUpgradeAffordability();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrades_menu);
        clickerButton = findViewById(R.id.clicker);
        brianButton = findViewById(R.id.brian);
        musicButton = findViewById(R.id.music);
        stickyFingersButton = findViewById(R.id.StickyFingers);
        sandwichSupremeButton = findViewById(R.id.SandwichSupreme);
        pbjtotal = findViewById(R.id.pbjtotal);
        back = findViewById(R.id.back);
        clickerdesc = findViewById(R.id.clickerdesc);
        briandesc = findViewById(R.id.briandesc);
        musicdesc = findViewById(R.id.musicdesc);
        stickyFingersdesc = findViewById(R.id.StickyFingersdesc);
        sandwichSupremeButton = findViewById(R.id.SandwichSupreme);
        sandwichSupremedesc = findViewById(R.id.SandwichSupremedesc);

        // Initialize all the data using the data sent from MainActivity
        Intent intent = getIntent();
        if (intent != null){
            total = intent.getIntExtra("PBJ", 0);
            clickerUp = intent.getIntExtra("clicker", 1);
            brianUp = intent.getIntExtra("brianUp", 0);
            music = intent.getBooleanExtra("music", false);
            stickyFingersUp = intent.getIntExtra("stickyFingersUp", 0);
            clickerCost = intent.getIntExtra("clickerCost", clickerCost);
            brianCost = intent.getIntExtra("brianCost", brianCost);
            stickyFingersCost = intent.getIntExtra("stickyFingersCost", stickyFingersCost);
            musicCost = intent.getIntExtra("musicCost", musicCost);
            sandwichSupremeCost = intent.getIntExtra("sandwichSupremeCost", sandwichSupremeCost);
            clickerdesc.setText("The Clicker upgrade gives +1 PBJ per click " + "(COST: " + clickerCost + ")");
            briandesc.setText("The BRIAN upgrade adds a dancing BRIAN to the bottom of the screen (Max 3). Brian will make 10 PBJs/sec. (COST: " + brianCost + ")");                stickyFingersdesc.setText("Increase BRIAN's peanut butter spread efficiency by +5 with sticky fingers! (per BRIAN) "+ "(COST: " + stickyFingersCost + ")");
            clickerButton.setText("Clicker: " + clickerUp);
            pbjtotal.setText("PB&Js: " + total);
            brianButton.setText("BRIAN: " + brianUp);
            musicButton.setText("The Holy Music of the PB&J Gods: " + music);
            stickyFingersButton.setText("Sticky Fingers: " + stickyFingersUp);
            newClicker = intent.getBooleanExtra("newClicker", newClicker);
            newBrian = intent.getBooleanExtra("newBrian", newBrian);
            newSandwich = intent.getBooleanExtra("newSandwich", newSandwich);
            newMusic = intent.getBooleanExtra("newMusic", newMusic);
            newSticky = intent.getBooleanExtra("newSticky", newSticky);
            music();
        }
        checkUpgradeAffordability();

        clickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total >= clickerCost) {
                    total -= clickerCost;
                    pbjtotal.setText("PB&Js: " + total);
                    clickerCost *= 1.1;
                    checkUpgradeAffordability();
                    clickerUp++;
                    clickerButton.setText("Clicker: " + clickerUp);
                    clickerdesc.setText("The Clicker upgrade gives +1 PBJ per click " + "(COST: " + clickerCost + ")");
                }
                else
                    Toast.makeText(Upgrades.this, "You're broke lol", Toast.LENGTH_SHORT).show();
            }
        });

        brianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total >= brianCost) {
                    if (brianUp < 3) {
                        total -= brianCost;
                        pbjtotal.setText("PB&Js: " + total);
                        brianCost *= 1.2;
                        checkUpgradeAffordability();
                        brianUp++;
                        brianButton.setText("BRIAN: " + brianUp);
                        briandesc.setText("The BRIAN upgrade adds a dancing BRIAN to the bottom of the screen (Max 3). Brian will make 10 PBJs/sec. (COST: " + brianCost + ")");
                        Intent serviceIntent = new Intent(Upgrades.this, PassiveIncomeService.class);
                        serviceIntent.putExtra("brianUp", brianUp);
                        serviceIntent.putExtra("stickyFingersUp", stickyFingersUp);
                        serviceIntent.putExtra("music", music);
                        startService(serviceIntent);
                    } else {
                        Toast.makeText(Upgrades.this, "You have reached the max amount of BRIANs", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(Upgrades.this, "You're broke lol", Toast.LENGTH_SHORT).show();
            }
        });

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total >= musicCost) {
                    if (!music) {
                        total -= musicCost;
                        pbjtotal.setText("PB&Js: " + total);
                        checkUpgradeAffordability();
                        music = true;
                        musicButton.setText("The Holy Music of the PB&J Gods: " + music);
                    } else
                        Toast.makeText(Upgrades.this, "You can only buy music once", Toast.LENGTH_SHORT).show();
                    music();
                    Intent serviceIntent = new Intent(Upgrades.this, PassiveIncomeService.class);
                    serviceIntent.putExtra("brianUp", brianUp);
                    serviceIntent.putExtra("stickyFingersUp", stickyFingersUp);
                    serviceIntent.putExtra("music", music);
                    startService(serviceIntent);
                }
                else
                    Toast.makeText(Upgrades.this, "You're broke lol", Toast.LENGTH_SHORT).show();
            }
        });

        stickyFingersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total >= stickyFingersCost) {
                    total -= stickyFingersCost;
                    pbjtotal.setText("PB&Js: " + total);
                    stickyFingersCost *= 1.2;
                    checkUpgradeAffordability();
                    stickyFingersUp++;
                    stickyFingersButton.setText("Sticky Fingers: " + stickyFingersUp);
                    stickyFingersdesc.setText("Increase BRIAN's peanut butter spread efficiency by +5 with sticky fingers! (per BRIAN) " + "(COST: " + stickyFingersCost + ")");
                    Intent serviceIntent = new Intent(Upgrades.this, PassiveIncomeService.class);
                    serviceIntent.putExtra("brianUp", brianUp);
                    serviceIntent.putExtra("stickyFingersUp", stickyFingersUp);
                    serviceIntent.putExtra("music", music);
                    startService(serviceIntent);
                }
                else
                    Toast.makeText(Upgrades.this, "You're broke lol", Toast.LENGTH_SHORT).show();
            }
        });

        sandwichSupremeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total >= sandwichSupremeCost) {
                    if (musicPlayer != null)
                        musicPlayer.stop();
                    total -= sandwichSupremeCost;
                    pbjtotal.setText("PB&Js: " + total);
                    checkUpgradeAffordability();
                    Intent end = new Intent(Upgrades.this, EndScreen.class);
                    startActivity(end);
                    finish();
                }
                else
                    Toast.makeText(Upgrades.this, "You're broke lol", Toast.LENGTH_SHORT).show();
            }
        });

        // Sends info back to the MainActivity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicPlayer != null)
                    musicPlayer.stop();
                Intent upgrades = new Intent(Upgrades.this, MarcoNgai.class);
                upgrades.putExtra("PBJ", total);
                upgrades.putExtra("clicker", clickerUp);
                upgrades.putExtra("brianUp", brianUp);
                upgrades.putExtra("music", music);
                upgrades.putExtra("stickyFingersUp", stickyFingersUp);
                upgrades.putExtra("clickerCost", clickerCost);
                upgrades.putExtra("brianCost", brianCost);
                upgrades.putExtra("stickyFingersCost", stickyFingersCost);
                upgrades.putExtra("musicCost", musicCost);
                upgrades.putExtra("sandwichSupremeCost", sandwichSupremeCost);
                upgrades.putExtra("newClicker", newClicker);
                upgrades.putExtra("newBrian", newBrian);
                upgrades.putExtra("newSandwich", newSandwich);
                upgrades.putExtra("newMusic", newMusic);
                upgrades.putExtra("newSticky", newSticky);
                startActivity(upgrades);
                finish();
            }
        });
    }

    private void music() {
        if (music) {
            musicPlayer = MediaPlayer.create(Upgrades.this, R.raw.peanutbutterjellymusic);
            if (musicPlayer != null) {
                musicPlayer.setLooping(true);
                musicPlayer.start();
            } else {
                Log.e("Upgrades", "Failed to initialize MediaPlayer");
            }
        }
    }

    private void checkUpgradeAffordability(){
        if (total < clickerCost) {
            clickerButton.setEnabled(false);
            clickerButton.setBackgroundColor(getColor(R.color.peanutbrown));
        }
        else {
            clickerButton.setEnabled(true);
            clickerButton.setBackgroundColor(getColor(R.color.jellypurp));
        }
        if (total < brianCost) {
            brianButton.setEnabled(false);
            brianButton.setBackgroundColor(getColor(R.color.peanutbrown));
        }
        else {
            brianButton.setEnabled(true);
            brianButton.setBackgroundColor(getColor(R.color.jellypurp));
        }
        if (total < stickyFingersCost) {
            stickyFingersButton.setEnabled(false);
            stickyFingersButton.setBackgroundColor(getColor(R.color.peanutbrown));
        }
        else {
            stickyFingersButton.setEnabled(true);
            stickyFingersButton.setBackgroundColor(getColor(R.color.jellypurp));
        }
        if (total < musicCost) {
            musicButton.setEnabled(false);
            musicButton.setBackgroundColor(getColor(R.color.peanutbrown));
        }
        else {
            musicButton.setEnabled(true);
            musicButton.setBackgroundColor(getColor(R.color.jellypurp));
        }
        if (total < sandwichSupremeCost) {
            sandwichSupremeButton.setEnabled(false);
            sandwichSupremeButton.setBackgroundColor(getColor(R.color.peanutbrown));
        }
        else {
            sandwichSupremeButton.setEnabled(true);
            sandwichSupremeButton.setBackgroundColor(getColor(R.color.jellypurp));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister BroadcastReceiver when activity is destroyed
        unregisterReceiver(passiveIncomeReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register BroadcastReceiver to receive updates from PassiveIncomeService
        ContextCompat.registerReceiver( this, passiveIncomeReceiver, new IntentFilter("passive_income_update"), ContextCompat.RECEIVER_EXPORTED);
    }
}

package edu.niu.csci.z1697841.infraredtransmitter;

import android.app.Activity;
import android.content.Intent;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;

/*
* Class: InfraredServiceActivity
* Inherits from: Activity
* Sends IR patternA to receiver
* */
public class InfraredServiceActivity extends Activity implements Animation.AnimationListener {

    Button sendBtn;
    Button rtlBtn;
    Button ltrBtn;
    ImageView couch;
    Button infoBtn;

    Animation rtlAnimation;
    Animation ltrAnimation;
    private ConsumerIrManager irManager;
    private boolean ltr = false;
    private boolean rtl = false;
    private int[] patternLTR = {
            // 38400 Hz
            // NEC protocol 0xFF02FD

            // Head:
            9000, 4500,

            // Address:
            563, 563, 563, 563, 563, 563, 563, 563,
            563, 563, 563, 563, 563, 563, 563, 563,

            // Address Inverse:
            563, 1688, 563, 1688, 563, 1688, 563, 1688,
            563, 1688, 563, 1688, 563, 1688, 563, 1688,

            // Command:
            563, 563, 563, 563, 563, 563, 563, 563,
            563, 563, 563, 563, 563, 1688, 563, 563,

            // Command Inverse:
            563, 1688, 563, 1688, 563, 1688, 563, 1688,
            563, 1688, 563, 1688, 563, 563, 563, 1688,

            // End Transmission
            563
    };
    private int[] patternRTL = {
            // 38400 Hz
            // NEC protocol 0xFF22DD

            // Head:
            9000, 4500,

            // Address:
            563, 563, 563, 563, 563, 563, 563, 563,
            563, 563, 563, 563, 563, 563, 563, 563,

            // Address Inverse:
            563, 1688, 563, 1688, 563, 1688, 563, 1688,
            563, 1688, 563, 1688, 563, 1688, 563, 1688,

            // Command:
            563, 563, 563, 563, 563, 1688, 563, 563,
            563, 563, 563, 563, 563, 1688, 563, 563,

            // Command Inverse:
            563, 1688, 563, 1688, 563, 563, 563, 1688,
            563, 1688, 563, 1688, 563, 563, 563, 1688,

            // End Transmission
            563
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infrared_service);

        irManager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
        sendBtn = findViewById(R.id.sendButton);
        ltrBtn = findViewById(R.id.ltrButton);
        rtlBtn = findViewById(R.id.rtlButton);
        couch = findViewById(R.id.couchView);
        infoBtn = findViewById(R.id.infoButton);
        rtlBtn.setBackgroundColor(BLUE);
        ltrBtn.setBackgroundColor(BLUE);
        ltrAnimation = AnimationUtils.loadAnimation(InfraredServiceActivity.this, R.anim.ltr_anim);
        rtlAnimation = AnimationUtils.loadAnimation(InfraredServiceActivity.this, R.anim.rtl_anim);

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfraredServiceActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        rtlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rtl = true;
                sendBtn.setEnabled(true);
                rtlBtn.setBackgroundColor(GREEN);
                ltrBtn.setBackgroundColor(BLUE);
            }
        });

        ltrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ltr = true;
                sendBtn.setEnabled(true);
                ltrBtn.setBackgroundColor(GREEN);
                rtlBtn.setBackgroundColor(BLUE);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!irManager.hasIrEmitter()) {
                    Toast.makeText(InfraredServiceActivity.this, "Device does not have IR capabilities", Toast.LENGTH_LONG).show();
                    return;
                }

                if (rtl) {
                    irManager.transmit(38400, patternRTL);
                    ltrBtn.setVisibility(View.INVISIBLE);
                    rtlBtn.startAnimation(rtlAnimation);
                } else if (ltr) {
                    irManager.transmit(38400, patternLTR);
                    rtlBtn.setVisibility(View.INVISIBLE);
                    ltrBtn.startAnimation(ltrAnimation);
                }
            }
        });

        rtlAnimation.setAnimationListener(this);
        ltrAnimation.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        sendBtn.setEnabled(false);
        couch.setImageAlpha(127);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        ltrBtn.setVisibility(View.VISIBLE);
        rtlBtn.setVisibility(View.VISIBLE);
        ltrBtn.setBackgroundColor(BLUE);
        rtlBtn.setBackgroundColor(BLUE);
        couch.setImageAlpha(255);
        ltr = false;
        rtl = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}

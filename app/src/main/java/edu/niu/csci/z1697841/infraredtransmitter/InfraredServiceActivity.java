package edu.niu.csci.z1697841.infraredtransmitter;

import android.app.Activity;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

    Animation rtlAnimation;
    Animation ltrAnimation;
    private ConsumerIrManager irManager;
    private boolean ltr = false;
    private boolean rtl = false;
    private int[] patternLEFT = {
            // 38400 Hz

            // collected from remote that came with IR receiver

            // head
            8986, 4614,

            494, 646, 494, 650, 486, 642,
            522, 618, 522, 614, 490, 646, 498, 642,
            498, 638, 470, 1782, 490, 1754, 502, 1750,
            498, 1754, 522, 1730, 490, 1758, 522, 1726,
            498, 1754, 494, 646, 490, 1754, 526, 1730,
            486, 646, 494, 1754, 522, 618, 494, 646,
            490, 642, 502, 1750, 522, 622, 486, 646,
            522, 1722, 498, 642, 502, 1750, 498, 1750,
            498, 1758, 490, 1000
    };
    private int[] patternRIGHT = {
            // 38400 Hz

            // collected from remote that came with IR receiver

            // head
            8986, 4614,

            494, 646, 494, 650, 486, 642,
            522, 618, 522, 614, 490, 646, 498, 642,
            498, 638, 470, 1782, 490, 1754, 502, 1750,
            498, 1754, 522, 1730, 490, 1758, 522, 1726,
            498, 1754, 494, 646, 490, 1754, 526, 1730,
            486, 646, 494, 1754, 522, 618, 494, 646,
            490, 642, 502, 1750, 522, 622, 486, 646,
            522, 1722, 498, 642, 502, 1750, 498, 1750,
            498, 1758, 490, 1000
    };
    /*
    * DOES NOT CURRENTLY WORK
    *
    *   potential reasons:
    *       math was off
    *       real world variables effected frequencies
    *       wrong protocol
    */
    private int[] patternA = {
            // 38400 Hz

            // 9ms leading pulse burst (16 times the pulse burst length used for a logical data bit)
            //    http://techdocs.altium.com/display/FPGA/NEC+Infrared+Transmission+Protocol

            9000, 4500,


            // lead in burst
            // http://www.hifi-remote.com/infrared/IR-PWM.shtml
            // 0, 108, 34, 0, 346, 173,

            // 8 bit address for the receiving device address
            // 1000 0001
            22, 65, 22, 22,
            22, 22, 22, 22,
            22, 22, 22, 22,
            22, 22, 22, 65,

            // 8 bit logical inverse for the receiving device address
            // 0111 1110
            22, 22, 22, 65,
            22, 65, 22, 65,
            22, 65, 22, 65,
            22, 65, 22, 22,

            // 8 bit command
            // 0001 0001
            22, 22, 22, 22,
            22, 22, 22, 65,
            22, 22, 22, 22,
            22, 22, 22, 65,

            // 8 bit logical inverse of command
            // 1110 1110
            22, 65, 22, 65,
            22, 65, 22, 22,
            22, 65, 22, 65,
            22, 65, 22, 22,

            // end of message transmission
            22

            // lead out burst
            // 22, 1427

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infrared_service);

        irManager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
        sendBtn = findViewById(R.id.sendButton);
        ltrBtn = findViewById(R.id.ltrButton);
        rtlBtn = findViewById(R.id.rtlButton);
        rtlBtn.setBackgroundColor(BLUE);
        ltrBtn.setBackgroundColor(BLUE);
        ltrAnimation = AnimationUtils.loadAnimation(InfraredServiceActivity.this, R.anim.ltr_anim);
        rtlAnimation = AnimationUtils.loadAnimation(InfraredServiceActivity.this, R.anim.rtl_anim);

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
                sendBtn.setEnabled(false);
                if (!irManager.hasIrEmitter()) {
                    Toast.makeText(InfraredServiceActivity.this, "Device does not have IR capabilities", Toast.LENGTH_LONG).show();
//                    return;
                } else {
                    irManager.transmit(38400, patternLEFT);
                }

                if (rtl) {
                    ltrBtn.setVisibility(View.INVISIBLE);
                    rtlBtn.startAnimation(rtlAnimation);
                } else if (ltr) {
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

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        ltrBtn.setVisibility(View.VISIBLE);
        rtlBtn.setVisibility(View.VISIBLE);
        ltrBtn.setBackgroundColor(BLUE);
        rtlBtn.setBackgroundColor(BLUE);
        ltr = false;
        rtl = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

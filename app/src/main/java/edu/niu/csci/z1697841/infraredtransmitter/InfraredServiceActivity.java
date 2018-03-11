package edu.niu.csci.z1697841.infraredtransmitter;

import android.app.Activity;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* Class: InfraredServiceActivity
* Inherits from: Activity
* Sends IR pattern to receiver
* */
public class InfraredServiceActivity extends Activity {
    private Method irWrite;
    private ConsumerIrManager irManager;
    private SparseArray<String> irData;
    private int[] pattern = {1901, 4453, 625, 1614, 625, 1588, 625, 1614, 625, 442, 625, 442, 625,
            468, 625, 442, 625, 494, 572, 1614, 625, 1588, 625, 1614, 625, 494, 572, 442, 651,
            442, 625, 442, 625, 442, 625, 1614, 625, 1588, 651, 1588, 625, 442, 625, 494, 598,
            442, 625, 442, 625, 520, 572, 442, 625, 442, 625, 442, 651, 1588, 625, 1614, 625,
            1588, 625, 1614, 625, 1588, 625, 48958};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infrared_service);

        irManager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
        final TextView displayTV = findViewById(R.id.displayTextView);
        Button sendBtn = findViewById(R.id.sendButton);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder values = new StringBuilder("Sending signal at Frequency:\n\n");
                if (irManager.hasIrEmitter()) {
                    values.append("38000");
                    irManager.transmit(38000, pattern);
                    displayTV.setText(values.toString());
                } else {
                    displayTV.setText("Device does not have IR Emitter");
                }
            }
        });
    }
}

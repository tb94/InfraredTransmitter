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
                StringBuilder values = new StringBuilder("IR Carrier Frequencies:\n\n");
                if (irManager.hasIrEmitter()) {
                    ConsumerIrManager.CarrierFrequencyRange[] frequencyRanges = irManager.getCarrierFrequencies();

                    for (ConsumerIrManager.CarrierFrequencyRange range : frequencyRanges) {
                        values.append(range.getMinFrequency());
                        values.append(" - ");
                        values.append(range.getMaxFrequency());
                        values.append("\n");
                    }
                    displayTV.setText(values.toString());
                } else {
                    displayTV.setText("Device does not have IR Emitter");
                }
            }
        });

/*        irData = new SparseArray<>();

        irData.put(R.id.sendButton, hexToDec("0000 006d 0022 0002 0152 00aa 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015"
                + " 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0015 0015 0696 0152 0055 0015 0e23"));
        irInit();*/
    }

    /*
    public void irSend(View view) {
        String data = irData.get(view.getId());
        if (data != null) {
            try {
                irWrite.invoke(consumerIrManager, data);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private String hexToDec(String irData) {
        List<String> list = new ArrayList<>(Arrays.asList(irData.split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16);
        list.remove(0); // seq1
        list.remove(0); // seq2

        for (int i = 0; i < list.size(); i++) {
            list.set(i, Integer.toString(Integer.parseInt(list.get(i), 16)));
        }

        frequency = (int) (1000000 / (frequency * 0.241246));
        list.add(Integer.toString(frequency));

        irData = " ";
        for (String s: list) {
            irData += s + ".";
        }

        return irData;
    }

    private void irInit() {
        consumerIrManager = (ConsumerIrManager)getSystemService(Context.CONSUMER_IR_SERVICE);
    }*/
}

package com.example.application;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;


public class MainActivity extends Activity {

    int count = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;
    BluetoothAdapter mBTA;
     TextView mTextView;
     TextView ntextVeiw;
     TextView itextVeiw;
     TextView ktextVeiw;

    private static final String TAG = "MyActivity";

    ArrayList<Integer> JBLRSSI = new ArrayList<Integer>();
    ArrayList<Integer> iphoneRSSI = new ArrayList<Integer>();
    ArrayList<Integer> beaconRSSI = new ArrayList<Integer>();
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        itextVeiw = (TextView) findViewById(R.id.textView2);
        ktextVeiw = (TextView) findViewById(R.id.textView3);
        ntextVeiw = (TextView) findViewById(R.id.textView4);
        mTextView = (TextView) findViewById(R.id.textView);
        //register local BT adapter
        mBTA = BluetoothAdapter.getDefaultAdapter();
        //check to see if there is BT on the Android device at all
        if (mBTA == null) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this, "No Bluetooth on this handset", duration).show();
        }
        //let's make the user enable BT if it isn't already
        if (!mBTA.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, 0xDEADBEEF);
        }
        //cancel any prior BT device discovery

        if (mBTA.isDiscovering()) {
            mBTA.cancelDiscovery();
        }

        //re-start discovery
        //mBTA.startDiscovery();

        //let's make a broadcast receiver to register our things

        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(BluetoothDevice.ACTION_FOUND);
        ifilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        ifilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, ifilter);


        //button that listens for when we press it
        View button = (View) findViewById(R.id.button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mBTA.startDiscovery();
                        mTextView.setText("Calculating Location");
                        break;
                }
                return false;
            }
        });



    }




    public void calculations()
    {
       float jbl_rssi = 0;
       float iphone_rssi = 0;
       float beacon_rssi = 0;

       int beacon_d = 0;
       int iphone_d = 0;
       int jbl_d = 0;

       if(JBLRSSI.size() < 3 || iphoneRSSI.size() < 3 || beaconRSSI.size() < 3)
       {
           mTextView.setText("Error: Didn't detect all three Beacons.");
       }
       else {


           for (int i = 0; i < 3; i++) {
               jbl_rssi += JBLRSSI.get(i);
               iphone_rssi += iphoneRSSI.get(i);
               beacon_rssi += beaconRSSI.get(i);
           }
           jbl_rssi = jbl_rssi / (-3);
           iphone_rssi = iphone_rssi / (-3);
           beacon_rssi = beacon_rssi / (-3);

           int jbl = Math.round(jbl_rssi);
           int iphone = Math.round(iphone_rssi);
           int beacon = Math.round(beacon_rssi);

           //Beacon sorting
           if (beacon <= 48) {
               beacon_d = 1;
           } else if (beacon <= 53) {
               beacon_d = 2;
           } else if (beacon <= 56) {
               beacon_d = 3;
           } else if (beacon <= 57) {
               beacon_d = 4;
           } else if (beacon <= 58) {
               beacon_d = 5;
           } else if (beacon <= 59) {
               beacon_d = 6;
           } else if (beacon <= 61) {
               beacon_d = 7;
           } else if (beacon <= 62) {
               beacon_d = 8;
           } else if (beacon <= 63) {
               beacon_d = 9;
           } else if (beacon <= 64) {
               beacon_d = 10;
           } else if (beacon <= 65) {
               beacon_d = 11;
           } else if (beacon <= 66) {
               beacon_d = 12;
           } else {
               beacon_d = 13;
           }
           itextVeiw.setText(String.valueOf(beacon_d));

           //Iphone Sorting
           if (iphone <= 48) {
               iphone_d = 1;
           } else if (iphone <= 49) {
               iphone_d = 2;
           } else if (iphone <= 52) {
               iphone_d = 3;
           } else if (iphone <= 53) {
               iphone_d = 4;
           } else if (iphone <= 54) {
               iphone_d = 5;
           } else if (iphone <= 56) {
               iphone_d = 6;
           } else if (iphone <= 57) {
               iphone_d = 7;
           } else if (iphone <= 58) {
               iphone_d = 8;
           } else if (iphone <= 59) {
               iphone_d = 9;
           } else if (iphone <= 60) {
               iphone_d = 10;
           } else if (iphone <= 61) {
               iphone_d = 11;
           } else if (iphone <= 62) {
               iphone_d = 12;
           } else {
               iphone_d = 13;
           }
           ktextVeiw.setText(String.valueOf(iphone_d));


           if (jbl <= 56) {
               jbl_d = 1;
           } else if (jbl <= 62) {
               jbl_d = 2;
           } else if (jbl <= 64) {
               jbl_d = 3;
           } else if (jbl <= 70) {
               jbl_d = 4;
           } else if (jbl <= 72) {
               jbl_d = 5;
           } else if (jbl <= 73) {
               jbl_d = 6;
           } else if (jbl <= 74) {
               jbl_d = 7;
           } else if (jbl <= 75) {
               jbl_d = 8;
           } else if (jbl <= 76) {
               jbl_d = 9;
           } else if (jbl <= 77) {
               jbl_d = 10;
           } else if (jbl <= 78) {
               jbl_d = 11;
           } else if (jbl <= 79) {
               jbl_d = 12;
           } else {
               jbl_d = 13;
           }
           ntextVeiw.setText(String.valueOf(jbl_d));

           double x = 1.0 * (jbl_d * jbl_d - (iphone_d * iphone_d) + 13 * 13) / (2.0 * 13);

           double y = 1.0 * (1.0 * jbl_d * jbl_d - 1.0 * (beacon_d * beacon_d) + 6.5 * 6.5 + 13 * 13) / (2.0 * 13) - (6.5 / 13.0) * x;
           String s = "Location: (" + Math.round(x) + "," + Math.round(y) + ")";

           mTextView.setText(s);
       }

    }


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);

                //mTextView.setText(deviceName + " - " + deviceHardwareAddress +" " + rssi);

                if(deviceHardwareAddress.equals("38:71:DE:E3:CE:2B"))
                {
                   // mTextView.setText("Iphone Got'em");
                    iphoneRSSI.add(rssi);
                }
                 if (deviceHardwareAddress.equals("0C:F3:EE:B5:B0:E3"))
                {
                    //mTextView.setText("Beacon Got'em");
                    beaconRSSI.add(rssi);
                }
                 if(deviceHardwareAddress.equals("B8:69:C2:CB:70:4D"))
                {
                    //mTextView.setText("JBL-GO Got'em");
                    JBLRSSI.add(rssi);
                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {
                //mTextView.setText("started");
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                //mTextView.setText("Ended");
                if(count > 1)
                {
                    count =0;
                    calculations();
                }
                else
                {
                    count++;
                    if (mBTA.isDiscovering()) {
                        mBTA.cancelDiscovery();
                    }
                    mBTA.startDiscovery();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }

}
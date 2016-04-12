package se.bjarntoft.detectionclient;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;


/**
 * Created by Andreas Bjärntoft on 2016-04-11.
 */
public class BluetoothService extends Service  {
    // Konstanter.
    public static final long BT_DISCOVERY_INTERVAL = 10 * 1000;     // millisekunder
    public static final long BT_DETECTION_INTERVAL = 20 * 1000;     // millisekunder

    // Arbetsobjekt.
    private BluetoothAdapter btAdapter;
    private Handler btDetectionHandler;

    // Arbetsvariabler.
    private Boolean btDetected = false;
    private String btDetectedMacAdress = "";
    private String lastRegisteredMacAdress = "";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        // Definierar filter för bluetooth-receiver.
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        // Registrerar bluetooth-receiver.
        registerReceiver(btReciver, filter);

        // Registrerar tråd för registrering av bluetooth-enhet.
        btDetectionHandler = new Handler();
        btDetectionHandler.post(btDetectionThread);

        // Startar bluetooth-scanning.
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.startDiscovery();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        // Avregistrerar tråd för registrering av bluetooth-enhet.
        btDetectionHandler.removeCallbacks(btDetectionThread);

        // Avregistrerar bluetooth-receiver.
        unregisterReceiver(btReciver);
    }


    private void sendDetection(String message) {
        // Registrerar bluetooth-enhet i databas.
        BluetoothDetectionTask bluetoothDetectionTask = new BluetoothDetectionTask();
        bluetoothDetectionTask.execute(this, message);
    }


    // Bluetooth-receiver.
    private final BroadcastReceiver btReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // Kontrollerar mottaget anrop till bluetooth-receiver.
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // Pausar bluetooth-scanning.
                new CountDownTimer(BT_DISCOVERY_INTERVAL, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        // Startar bluetooth-scanning igen.
                        btAdapter.startDiscovery();
                    }
                }.start();
            } else if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Identifierar funnen bluetooth-enhet.
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();

                // Kontrollerar om bluetooth-enheten tillhör systemet.
                if(name != null && name.equals("RN-42")) {
                    btDetectedMacAdress = device.getAddress();
                    btDetected = true;
                }
            }
        }
    };


    // Tråd för registrering av bluetooth-enhet.
    private final Runnable btDetectionThread = new Runnable() {
        @Override
        public void run() {
            // Kontrollerar om en bluetooth-enhet som tillhör systemet har detekterats.
            if(btDetected) {
                // Kontrollerar så att registreringen i databasen inte redan är gjord.
                if(!btDetectedMacAdress.equals(lastRegisteredMacAdress)) {
                    sendDetection(btDetectedMacAdress);
                    lastRegisteredMacAdress = btDetectedMacAdress;
                }

                btDetected = false;
            } else {
                // Kontrollerar så att registreringen i databasen inte redan är gjord.
                if(!lastRegisteredMacAdress.equals("NULL")) {
                    sendDetection("NULL");
                    lastRegisteredMacAdress = "NULL";
                }
            }

            // Tidsinställer nästa registrering i databasen.
            btDetectionHandler.postDelayed(this, BT_DETECTION_INTERVAL);
        }
    };
}

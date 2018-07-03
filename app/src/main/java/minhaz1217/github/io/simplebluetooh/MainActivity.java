package minhaz1217.github.io.simplebluetooh;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import android.bluetooth.BluetoothSocket;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    boolean bluetoothEnabled = false;
    //public static String[] pairedDeviceArray = new String[200];
    public static ArrayList<String> pairedDeviceListArray = new ArrayList<String>();
    public static ArrayList<String> pairedDeviceAddress = new ArrayList<String>();
    BluetoothAdapter bluetoothAdapter;
    int pairedDeviceRequestCode = 1;
    BluetoothSocket pairedDeviceSocket;
    Set<BluetoothDevice> pairedDevice;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //static final UUID myUUID = UUID.randomUUID();

    TextView connectionStatus, lightStatus, fanStatus;
    String messages[] = {"Not Connected", "Light Status: OFF", "Light Status: ON", "Fan Status: OFF", "Fan Status: ON"};
    int positive = Color.parseColor("#00c853");
    int negative = Color.parseColor("#d50000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Log.w("STARTED", "HELLO");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        connectionStatus = findViewById(R.id.connectionStatus);
        lightStatus = findViewById(R.id.tv_light);
        fanStatus = findViewById(R.id.tv_fan);
        connectionStatus.setText(messages[0]);
        connectionStatus.setTextColor(negative);


    }


    public void showMessage(String str){
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }




    public void clicked_b_connect(View view){

        //showMessage("CLICKED CONNECT");

        bluetoothEnabled = bluetoothAdapter.isEnabled(); // chcking if the bluetooth is e enabled.
        if(!bluetoothEnabled){
            // if the bluetooth is not enabled then show a message to enable bluetooth and enabling the bluetooth.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth Disabled");
            builder.setMessage("Turn ON Bluetooth?");
            builder.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivity(enableBluetoothIntent);
                            bluetoothEnabled = bluetoothAdapter.isEnabled();
                        }
                    }

            );

            builder.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showMessage("App Can't work without bluetooth being enabled.");
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            // bluetooth is enabled so we'll generate the paired device list.
            Intent startPairedDeviceList = new Intent(MainActivity.this, PairdDevicesList.class);


            Bundle bundle = new Bundle();
            // add paired device list to the pairedDeviceListArray

            if(bluetoothAdapter.getBondedDevices().size() > 0){
                // finding the list of paired devices and using them to generate a list of their name and address
                pairedDevice = bluetoothAdapter.getBondedDevices();
                pairedDeviceListArray.clear();
                pairedDeviceAddress.clear();
                for(BluetoothDevice device : pairedDevice ){
                    pairedDeviceListArray.add(device.getName());
                    pairedDeviceAddress.add(device.getAddress());
                }
            }


            bundle.putStringArrayList("pairedDeviceList", pairedDeviceListArray );
            startPairedDeviceList.putExtras(bundle);
            startActivityForResult(startPairedDeviceList, pairedDeviceRequestCode);

        }





    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == pairedDeviceRequestCode){
            if(resultCode == RESULT_OK){
                String str = data.getStringExtra("pairedDeviceResult");
                int selection = Integer.parseInt(str );

                for(BluetoothDevice device : pairedDevice){
                    if(device.getAddress().equals(pairedDeviceAddress.get(selection)) && device.getName().equals(pairedDeviceListArray.get(selection)) ){
                        try {
                            pairedDeviceSocket = device.createRfcommSocketToServiceRecord(myUUID);
                            pairedDeviceSocket.connect();
                            break;
                        }catch (Exception e){
                            Log.e("ERROR", e.getMessage());
                        }
                    }
                }

                if(pairedDeviceSocket.isConnected()){
                    connectionStatus.setText("Conencted: " + pairedDeviceSocket.getRemoteDevice().getName());
                    connectionStatus.setTextColor(positive);
                    showMessage("CONNECTED TO: " + pairedDeviceSocket.getRemoteDevice().getName());
                }else{
                    showMessage("Unable To Connect");
                }
            }
        }


    }
    public void clicked_b_lightOn(View view){

        if(pairedDeviceSocket != null){ // checking if the socket is busy or not
            try{
                pairedDeviceSocket.getOutputStream().write("1".getBytes()); // sending 1 to turn on light
                lightStatus.setText(messages[2]);
                lightStatus.setTextColor(positive);
            }catch (IOException e){
                showMessage("Unable to send message. Check if bluetooth is connected.");
                Log.e("IO_ERROR", e.getMessage());
            }
        }




    }
    public void clicked_b_lightOff(View view){
        if(pairedDeviceSocket != null){ // checking if the socket is busy or not
            try{
                pairedDeviceSocket.getOutputStream().write("2".getBytes()); // sending 2 to turn off light
                lightStatus.setText(messages[1]);
                lightStatus.setTextColor(negative);

            }catch (IOException e){
                showMessage("Unable to send message. Check if bluetooth is connected.");
                Log.e("IO_ERROR", e.getMessage());
            }
        }
    }
    public void clicked_b_fanOn(View view){
        if(pairedDeviceSocket != null){ // checking if the socket is busy or not
            try{
                pairedDeviceSocket.getOutputStream().write("3".getBytes()); // sending 3 to turn on fan
                fanStatus.setText(messages[4]);
                fanStatus.setTextColor(positive);
            }catch (IOException e){
                showMessage("Unable to send message. Check if bluetooth is connected.");
                Log.e("IO_ERROR", e.getMessage());
            }
        }
    }
    public void clicked_b_fanOff(View view){
        if(pairedDeviceSocket != null){ // checking if the socket is busy or not
            try{
                pairedDeviceSocket.getOutputStream().write("4".getBytes()); // sending 4 to turn off fan
                fanStatus.setText(messages[3]);
                fanStatus.setTextColor(negative);

            }catch (IOException e){
                showMessage("Unable to send message. Check if bluetooth is connected.");
                Log.e("IO_ERROR", e.getMessage());
            }
        }
    }


}

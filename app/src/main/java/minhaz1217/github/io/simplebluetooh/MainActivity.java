package minhaz1217.github.io.simplebluetooh;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import android.bluetooth.BluetoothSocket;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("STARTED", "HELLO");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        setContentView(R.layout.activity_main);

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
                //showMessage(str);
                int selection = Integer.parseInt(str );
                //showMessage(pairedDeviceListArray.get(selection));

                for(BluetoothDevice device : pairedDevice){
                    if(device.getAddress().equals(pairedDeviceAddress.get(selection)) && device.getName().equals(pairedDeviceListArray.get(selection)) ){
                        //showMessage("MATCH FOUND: " + device.getName());
                        try {
                            pairedDeviceSocket = device.createRfcommSocketToServiceRecord(myUUID);
                            pairedDeviceSocket.connect();
                            showMessage("CONNECTING");
                            break;
                        }catch (Exception e){
                            Log.e("ERROR", e.getMessage());
                        }
                    }
                }

                if(pairedDeviceSocket.isConnected()){
                    showMessage("CONNECTED TO: " + pairedDeviceSocket.getRemoteDevice().getName());
                }else{
                    showMessage("NOT CONNECTED");
                }
                //showMessage(str);
            }
        }


    }
    public void clicked_b_lightOn(View view){
        showMessage("CLICKED Light ON");
        if(pairedDeviceSocket != null){ // checking if the socket is busy or not
            try{
                pairedDeviceSocket.getOutputStream().write("1".getBytes()); // sending 1 to turn on light
            }catch (IOException e){
                Log.e("IO_ERROR", e.getMessage());
            }
        }



    }
    public void clicked_b_lightOff(View view){
        showMessage("CLICKED Light OFF");
        if(pairedDeviceSocket != null){ // checking if the socket is busy or not
            try{
                pairedDeviceSocket.getOutputStream().write("2".getBytes()); // sending 2 to turn off light
            }catch (IOException e){
                Log.e("IO_ERROR", e.getMessage());
            }
        }
    }
    public void clicked_b_fanOn(View view){
        showMessage("CLICKED Fan ON");
        if(pairedDeviceSocket != null){ // checking if the socket is busy or not
            try{
                pairedDeviceSocket.getOutputStream().write("3".getBytes()); // sending 3 to turn on fan
            }catch (IOException e){
                Log.e("IO_ERROR", e.getMessage());
            }
        }
    }
    public void clicked_b_fanOff(View view){
        showMessage("CLICKED Fan OFF");
        if(pairedDeviceSocket != null){ // checking if the socket is busy or not
            try{
                pairedDeviceSocket.getOutputStream().write("4".getBytes()); // sending 4 to turn off fan
            }catch (IOException e){
                Log.e("IO_ERROR", e.getMessage());
            }
        }
    }


}

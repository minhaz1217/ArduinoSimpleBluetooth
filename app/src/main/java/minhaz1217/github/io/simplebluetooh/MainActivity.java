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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    boolean bluetoothEnabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("STARTED", "HELLO");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showMessage(String str){
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }




    public void clicked_b_connect(View view){
        showMessage("CLICKED CONNECT");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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

        }





    }
    public void clicked_b_lightOn(View view){
        showMessage("CLICKED Light ON");
    }
    public void clicked_b_lightOff(View view){
        showMessage("CLICKED Light OFF");
    }
    public void clicked_b_fanOn(View view){
        showMessage("CLICKED Fan ON");
    }
    public void clicked_b_fanOff(View view){
        showMessage("CLICKED Fan OFF");
    }


}

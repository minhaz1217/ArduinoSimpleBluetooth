package minhaz1217.github.io.simplebluetooh;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PairdDevicesList extends ListActivity {
    public static ArrayList<String> deviceListArray = new ArrayList<>();
    public void showMessage(String str){
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deviceListArray.add("1");
        deviceListArray.add("1");
        deviceListArray.add("1");
        deviceListArray.add("1");deviceListArray.add("1");

        Intent myIntent = getIntent();
        Bundle receiveBundle = myIntent.getExtras();
        //String[] deviceList = receiveBundle.getStringArray("pairedDeviceList");
        ArrayList<String> receivedArrayList = receiveBundle.getStringArrayList("pairedDeviceList");

        //showMessage(receiveBundle.getStringArray("pairedDeviceList")[0].toString()+" ");

        //String[] array = receiveBundle.getStringArray("pairedDeviceList");
        //Log.v("DEVICE LIST", receieveBundle.size() +"" );
        //showMessage("SIZE: " + array[0]);

        ArrayList<String> test = new ArrayList<String>();
        test.add("1");
        test.add("1");
        test.add("1");
        test.add("1");
        String[] testArr = {"1B", "2B", "3B"};
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, receivedArrayList
                ));
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setTextFilterEnabled(true);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // on an item click sending the index of the item back to the main activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("pairedDeviceResult", i + "" );
                setResult(RESULT_OK, resultIntent);
                finish(); // ending this activity


                //Log.d("HELLO", i + "");
                //Toast.makeText(getApplicationContext(), i + "", Toast.LENGTH_SHORT).show();

            }
        });


        //setContentView(R.layout.activity_paird_devices_list);
    }
}

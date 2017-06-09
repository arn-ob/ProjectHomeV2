package com.example.arnob.projecthome;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static android.R.attr.delay;
import static android.R.attr.publicKey;

public class Main_Activity extends AppCompatActivity {

    //s is button variable
    // sw is button status check variable


    private final String DEVICE_ADDRESS="98:D3:31:FD:45:1F";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");  //Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    public String getData;
    Button Disconnect,TryConnect;
    TextView textView;
    Switch s1,s2,s3,s4,s5,s6;
    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;
    String ReceiveString = "Null";
    public boolean sw1 = false,
            sw2 = false,
            sw3 = false,
            sw4 = false,
            sw5 = false,
            sw6 = false;
    String CheckOne = "0" ,
            CheckTwo = "0",
            CheckThree = "0",
            CheckFour = "0",
            CheckFive = "0",
            CheckSix = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Disconnect = (Button)findViewById(R.id.disbutton);
        TryConnect = (Button)findViewById(R.id.TryConn);
        textView = (TextView)findViewById(R.id.ConncetionStatus);
        s1 = (Switch)findViewById(R.id.switch1);
        s2 = (Switch)findViewById(R.id.switch2);
        s3 = (Switch)findViewById(R.id.switch3);
        s4 = (Switch)findViewById(R.id.switch4);
        s5 = (Switch)findViewById(R.id.switch5);
        s6 = (Switch)findViewById(R.id.switch6);


        try{
            cnt();


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Cant Connect to BlueTooth",Toast.LENGTH_SHORT).show();
        }


        //MakeConnection();




    }


    public void MakeConnection(){
        try {
            if (BTinit()) {
                if (BTconnect()) {
                   //Ask for data

                    deviceConnected = true;
                    beginListenForData();

                    textView.append("\nConnected and Data Lising!\n");

                }
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.getStackTrace();
            textView.append("\nConnection Got Problem!\n");
        }
    }

    void beginListenForData()
    {

        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 0)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);

                            final String s = new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                public void run()
                                {
                                    //textView.append(s);
                                    //CheckStateOFSwitch(getData);
                                    //CheckStateOFSwitch(s);
                                    //textView.append(s);
                                    //ReceiveString = s.toString();
                                    if(s.equals("1")){
                                        CheckOne = "1";
                                    }

                                    if(s.equals("2")){
                                        CheckTwo = "2";
                                    }

                                    if(s.equals("3")){
                                        CheckThree = "3";
                                    }

                                    if(s.equals("4")){
                                        CheckFour = "4";
                                    }

                                    if(s.equals("5")){
                                        CheckFive = "5";
                                    }

                                    if(s.equals("6")){
                                        CheckSix = "6";
                                    }


                                }
                            });

                        }
                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }




    public boolean BTinit()
    {
        try {
            boolean found = false;
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Toast.makeText(getApplicationContext(), "Device doesnt Support Bluetooth", Toast.LENGTH_SHORT).show();
            }
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableAdapter, 0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
            if (bondedDevices.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
            } else {
                for (BluetoothDevice iterator : bondedDevices) {
                    if (iterator.getAddress().equals(DEVICE_ADDRESS)) {
                        device = iterator;
                        found = true;
                        break;
                    }
                }
            }
            return found;

        }catch (Exception e){

            Toast.makeText(getApplicationContext(), "Device Got Problem With Bluetooth", Toast.LENGTH_SHORT).show();
            return false;

        }
    }

    public boolean BTconnect ()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return connected;
    }

    public void TryToConnect(View view){

            // Code Worked
            cnt();

    }




    public void btnConnect(View view) throws Exception{

        cnt();
    }

    public void cnt(){
        try{
            MakeConnection();

            // Code Worked
            // Calling The acknolodgement state
            GetState();
            Thread.sleep(100);
           // RecheckSwState();

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Connection Got problem",Toast.LENGTH_SHORT).show();
        }
    }


    // Disconnect the connection
    public void stopConnection () throws IOException {
        stopThread = true;
        outputStream.close();
        inputStream.close();
        socket.close();

        deviceConnected=false;
        textView.append("\nConnection Closed!\n");
    }


    // Send data to arduino
    public void sendData(String sendDataString){
        String string = sendDataString.toString();
        string.concat("\n");
        try {
            outputStream.write(string.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void GetState() throws InterruptedException{


        // Sw 1
        try {
            sendData("1");
            beginListenForData();
            if (CheckOne.equals("1")) {
                s1.setChecked(true);
            }if (CheckOne.equals("0")) {
                s1.setChecked(false);
            }
            else {
                s1.setChecked(false);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Cant Get Data 1",Toast.LENGTH_SHORT).show();
        }

        //Give a sleep

        Thread.sleep(50);


        // Sw 2
        try {
            sendData("2");
            beginListenForData();
            if (CheckTwo.equals("2")) {
                s2.setChecked(true);
            }if (CheckTwo.equals("0")) {
                s2.setChecked(false);
            }
            else{
                s2.setChecked(false);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Cant Get Data 2",Toast.LENGTH_SHORT).show();
        }
        //Give a sleep

        Thread.sleep(50);

        // Sw 3
        try {
            sendData("3");
            beginListenForData();
            if (CheckThree.equals("3")) {
                s3.setChecked(true);
            }if (CheckThree.equals("0")) {
                s3.setChecked(false);
            }
            else{
                s3.setChecked(false);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Cant Get Data 3",Toast.LENGTH_SHORT).show();
        }

        //Give a sleep

        Thread.sleep(50);

        // Sw 4
        try {
            sendData("4");
            beginListenForData();
            if (CheckFour.equals("4")) {
                s4.setChecked(true);
            }if (CheckFour.equals("0")) {
                s4.setChecked(false);
            }
            else{
                s4.setChecked(false);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Cant Get Data 4",Toast.LENGTH_SHORT).show();
        }

        //Give a sleep

        Thread.sleep(50);

        // Sw 5
        try {
            sendData("5");
            beginListenForData();
            if (CheckFive.equals("5")) {
                s5.setChecked(true);
            }if (CheckFive.equals("0")) {
                s5.setChecked(false);
            }
            else{
                s5.setChecked(false);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Cant Get Data 5",Toast.LENGTH_SHORT).show();
        }

        //Give a sleep

        Thread.sleep(50);

        // SW 6
        try {
            sendData("6");
            beginListenForData();
            if (CheckSix.equals("6")) {
                s6.setChecked(true);
            }if (CheckSix.equals("0")) {
                s6.setChecked(false);
            }
            else{
                s6.setChecked(false);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Cant Get Data 6",Toast.LENGTH_SHORT).show();
        }
        // GetState(); // it take infinite loop

        //RecheckSwState();

    }


    public void RecheckSwState() throws InterruptedException {

        // Sw 1 Check
        if (CheckOne.equals("1")) {
            s1.setChecked(true);
        }if (CheckOne.equals("0")) {
            s1.setChecked(false);
        }
        else {
            s1.setChecked(false);
        }



        // Sw 2 Check
        if (CheckTwo.equals("2")) {
            s2.setChecked(true);
        }if (CheckTwo.equals("0")) {
            s2.setChecked(false);
        }
        else{
            s2.setChecked(false);
        }



        // Sw 3 Check
        if (CheckThree.equals("3")) {
            s3.setChecked(true);
        }if (CheckThree.equals("0")) {
            s3.setChecked(false);
        }
        else{
            s3.setChecked(false);
        }



        // Sw 4 Check
        if (CheckFour.equals("4")) {
            s4.setChecked(true);
        }if (CheckFour.equals("0")) {
            s4.setChecked(false);
        }
        else{
            s4.setChecked(false);
        }



        // Sw 5 Check
        if (CheckFive.equals("5")) {
            s5.setChecked(true);
        }if (CheckFive.equals("0")) {
            s5.setChecked(false);
        }
        else{
            s5.setChecked(false);
        }

        // Sw 6 Check
        if (CheckSix.equals("6")) {
            s6.setChecked(true);
        }if (CheckSix.equals("0")) {
            s6.setChecked(false);
        }
        else{
            s6.setChecked(false);
        }

    }

    public void OnOffState(){

        if(CheckOne.equals("1")){
            CheckOne = "1";
        }else{ CheckOne = "0";}

        if(CheckTwo.equals("2")){
            CheckTwo = "2";
        }else{ CheckTwo = "0";}

        if(CheckThree.equals("3")){
            CheckThree = "3";
        }else{ CheckThree = "0";}

        if(CheckFour.equals("4")){
            CheckFour = "4";
        }else{ CheckFour = "0";}

        if(CheckFive.equals("5")){
            CheckFive = "5";
        }else{ CheckFive = "0";}

        if(CheckSix.equals("6")){
            CheckSix = "6";
        }else{ CheckSix = "0";}

    }



}

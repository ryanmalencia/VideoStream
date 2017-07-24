package com.example.ryanm.videostream;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    EditText address;
    Button connect;
    VideoView stream;
    MediaController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        address = (EditText) findViewById(R.id.address);
        connect = (Button) findViewById(R.id.connect);
        stream = (VideoView) findViewById(R.id.stream);

        String filename = "/address";
        File file = new File(getFilesDir() + filename);
        if(file.exists()){
            try {
                FileInputStream fis = openFileInput("address");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                int count = 0;
                while (count < 1){
                    String str;
                    if((str = br.readLine()) != null){
                        address.setText(str);
                    }
                    count++;
                }
                br.close();
                isr.close();
                fis.close();
            }
            catch(Exception e)
            {
                Log.d("file","Error reading address file");
            }
        }
    }

    public void onConnect(View view){
        String streamLoc = address.getEditableText().toString();
        saveAddress(streamLoc);
        playStream(streamLoc);
    }

    private void playStream(String location) {
        Uri src = Uri.parse(location);
        if(src == null){
            Toast.makeText(MainActivity.this,"Invalid Location", Toast.LENGTH_LONG).show();
        }
        else{
            stream.setVideoURI(src);
            controller = new MediaController(this);
            stream.setMediaController(controller);
            stream.start();
            Toast.makeText(MainActivity.this, "Connect: " + src, Toast.LENGTH_LONG).show();
        }
    }

    private void saveAddress(String location) {
        if(location != null) {
            try {
                FileOutputStream fos = openFileOutput("address", MODE_PRIVATE);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                osw.write(location);
                osw.close();
                fos.close();
            }
            catch(IOException error){
                Log.d("file","Error writing address file");
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stream.stopPlayback();
    }
}

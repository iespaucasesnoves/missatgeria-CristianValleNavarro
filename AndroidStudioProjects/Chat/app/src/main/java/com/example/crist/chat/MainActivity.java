package com.example.crist.chat;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private ReceptorXarxa receptor;
    private Button botonLogin;
    private EditText edittextUsuari;
    private EditText edittextPassw;
    String url = "http://iesmantpc.000webhostapp.com/public/login/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receptor = new ReceptorXarxa();
        this.registerReceiver(receptor, filter);

        botonLogin = findViewById(R.id.bLogin);
        edittextPassw = findViewById(R.id.etPassw);
        edittextUsuari = findViewById(R.id.etUsuari);

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> param = new HashMap<>();
                param.put("nom", edittextUsuari.getText().toString());
                param.put("password", edittextPassw.getText().toString());

                LoginChat loginchat = new LoginChat(param, getBaseContext());
                loginchat.execute(url);

            }
        });
    }



    public void onStart() {
        super.onStart();
        //Obtenim un gestor de les connexions de xarxa
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Obtenim l’estat de la xarxa
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //Obtenim l’estat de la xarxa mòbil
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean connectat3G = networkInfo.isConnected();

        //Obtenim l’estat de la xarxa Wifi
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean connectatWifi = networkInfo.isConnected();

        //Si està connectat
        if (networkInfo != null && connectat3G) {
            //Xarxa OK
            Toast.makeText(this, "Xarxa ok, conectat3G", Toast.LENGTH_LONG).show();
        } else if (networkInfo != null && connectatWifi){
            //Xarxa OK
            Toast.makeText(this, "Xarxa ok, conectat Wifi", Toast.LENGTH_LONG).show();
        } else {
            //Xarxa no disponible
            Toast.makeText(this, "Xarxa no disponible", Toast.LENGTH_LONG).show();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        //Donam de baixa el receptor de broadcast quan es destrueix l’aplicació
        if (receptor != null) {
            this.unregisterReceiver(receptor);
        }
    }

}

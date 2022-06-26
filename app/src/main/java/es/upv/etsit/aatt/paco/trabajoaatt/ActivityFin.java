package es.upv.etsit.aatt.paco.trabajoaatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivityFin extends AppCompatActivity {
    private static final String TAG = "API_REST";

    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;

    public String pueblo, provincia, dir_viento, est_cielo,temp_max, temp_min, prob_precip, vel_viento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_foreground);

        tv1 = (TextView) findViewById(R.id.datoProvincia);
        tv2 = (TextView) findViewById(R.id.datoPueblo);
        tv3 = (TextView) findViewById(R.id.datoTempMax);
        tv4 = (TextView) findViewById(R.id.datoTempMin);
        tv5 = (TextView) findViewById(R.id.datoPrecip);
        tv6 = (TextView) findViewById(R.id.datoDirViento);
        tv7 = (TextView) findViewById(R.id.datoVelViento);
        tv8 = (TextView) findViewById(R.id.datoEstCielo);


        String provincia = getIntent().getStringExtra("Provincia");
        String pueblo = getIntent().getStringExtra("Nombre");
        String temp_max = getIntent().getStringExtra("tempMax");
        String temp_min = getIntent().getStringExtra("tempMin");
        String prob_precip = getIntent().getStringExtra("probPre");
        String dir_viento = getIntent().getStringExtra("dirViento");
        String vel_viento = getIntent().getStringExtra("velViento");
        String est_cielo = getIntent().getStringExtra("estCielo");

        tv1.setText(provincia);
        tv2.setText(pueblo);
        tv3.setText((temp_max) + " ºC");
        tv4.setText((temp_min) + " ºC");
        tv5.setText((prob_precip) + " %");
        tv6.setText(dir_viento);
        tv7.setText((vel_viento) + " km/h");
        tv8.setText(est_cielo);
    }
    //Boton Inicio
    public void start (View view){
        Intent inicio = new Intent(this, MainActivity.class);
        startActivity(inicio);
    }
}
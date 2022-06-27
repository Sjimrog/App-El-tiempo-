package es.upv.etsit.aatt.paco.trabajoaatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityFin extends AppCompatActivity {
    private static final String TAG = "API_REST";

    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;
    private ImageView est;

    private String pueblo, provincia, dir_viento, est_cielo,temp_max, temp_min, prob_precip, vel_viento, id_pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);

        //Establecemos el logo en la action Bar
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
        est = (ImageView)findViewById(R.id.img_estCielo);

        id_pro = getIntent().getStringExtra("Id_provincia");
        provincia = getIntent().getStringExtra("Provincia");
        pueblo = getIntent().getStringExtra("Nombre");
        temp_max = getIntent().getStringExtra("tempMax");
        temp_min = getIntent().getStringExtra("tempMin");
        prob_precip = getIntent().getStringExtra("probPre");
        dir_viento = getIntent().getStringExtra("dirViento");
        vel_viento = getIntent().getStringExtra("velViento");
        est_cielo = getIntent().getStringExtra("estCielo");

        tv1.setText(provincia);
        tv2.setText(pueblo);
        tv3.setText((temp_max) + " ºC");
        tv4.setText((temp_min) + " ºC");
        tv5.setText((prob_precip) + " %");
        tv6.setText(dir_viento);
        tv7.setText((vel_viento) + " km/h");
        tv8.setText(est_cielo);
        Imagenes(est_cielo);
    }

    //Boton para pasar a la activity de los municipios
    public void Municipios(View view){
        Intent municipios = new Intent(this, ActivityMun.class);
        municipios.putExtra("ProvinciaElegida", provincia);
        municipios.putExtra("IdProvinciaElegida", id_pro);
        startActivity(municipios);
    }

    //Boton para pasar a la actvity de las provincias
    public void Provincias(View view){
        Intent inicio = new Intent(this, MainActivity.class);
        startActivity(inicio);
    }

    //establece la imagen segun el estado del cielo
    public void Imagenes(String estado_cielo){
        Drawable x = null;
        switch (estado_cielo) {
            case ("Bruma"):
                x = getResources().getDrawable(R.drawable.bruma);
                break;
            case ("Calima"):
                x = getResources().getDrawable(R.drawable.calima);
                break;
            case ("Cubierto"):
                x = getResources().getDrawable(R.drawable.cubierto);
                break;
            case ("Cubierto con lluvia"):
                x = getResources().getDrawable(R.drawable.cubiertolluvia);
                break;
            case ("Cubierto con lluvia escasa"):
                x = getResources().getDrawable(R.drawable.cubiertolluviaescasa);
                break;
            case ("Cubierto con nieve"):
                x = getResources().getDrawable(R.drawable.cubiertonieve);
                break;
            case ("Cubierto con nieve escasa"):
                x = getResources().getDrawable(R.drawable.cubiertonieveescasa);
                break;
            case ("Cubierto con tormenta"):
                x = getResources().getDrawable(R.drawable.cubiertotormenta);
                break;
            case ("Despejado"):
                x = getResources().getDrawable(R.drawable.despejado);
                break;
            case ("Intervalos nubosos con nieve"):
                x = getResources().getDrawable(R.drawable.intervalonubososnieve);
                break;
            case ("Intervalos nubosos con nieve escasa"):
                x = getResources().getDrawable(R.drawable.intervalonubososnieveescasa);
                break;
            case ("Intervalos nubosos con tormenta"):
                x = getResources().getDrawable(R.drawable.intervalonubosostormenta);
                break;
            case ("Intervalos nubosos con tormenta y lluvia escasa"):
                x = getResources().getDrawable(R.drawable.intervalonubosostormentalluviaescasa);
                break;
            case ("Intervalos nubosos"):
                x = getResources().getDrawable(R.drawable.intervalosnubosos);
                break;
            case ("Intervalos nubosos con lluvia"):
                x = getResources().getDrawable(R.drawable.intervalosnubososlluvia);
                break;
            case ("Intervalos nubosos con lluvia escasa"):
                x = getResources().getDrawable(R.drawable.intervalosnubososlluviaescasa);
                break;
            case ("Muy nuboso"):
                x = getResources().getDrawable(R.drawable.muynuboso);
                break;
            case ("Muy nuboso con lluvia"):
                x = getResources().getDrawable(R.drawable.muynubosolluvia);
                break;
            case ("Muy nuboso con lluvia escasa"):
                x = getResources().getDrawable(R.drawable.muynubosolluviaescasa);
                break;
            case ("Muy nuboso con nieve"):
                x = getResources().getDrawable(R.drawable.muynubosonieve);
                break;
            case ("Muy nuboso con nieve escasa"):
                x = getResources().getDrawable(R.drawable.muynubosonieveescasa);
                break;
            case ("Muy nuboso con tormenta"):
                x = getResources().getDrawable(R.drawable.muynubosotormenta);
                break;
            case ("Muy nuboso con tormenta y lluvia escasa"):
                x = getResources().getDrawable(R.drawable.muynubosotormentalluviaescasa);
                break;
            case ("Niebla"):
                x = getResources().getDrawable(R.drawable.niebla);
                break;
            case ("Nubes altas"):
                x = getResources().getDrawable(R.drawable.nubesaltas);
                break;
            case ("Nuboso"):
                x = getResources().getDrawable(R.drawable.nuboso);
                break;
            case ("Nuboso con lluvia"):
                x = getResources().getDrawable(R.drawable.nubosolluvia);
                break;
            case ("Nuboso con lluvia escasa"):
                x = getResources().getDrawable(R.drawable.nubosolluviaescasa);
                break;
            case ("Nuboso con nieve"):
                x = getResources().getDrawable(R.drawable.nubosonieve);
                break;
            case ("Nuboso con nieve escasa"):
                x = getResources().getDrawable(R.drawable.nubosonieveescasa);
                break;
            case ("Nuboso con tormenta"):
                x = getResources().getDrawable(R.drawable.nubosotormenta);
                break;
            case ("Nuboso con tormenta y lluvia escasa"):
                x = getResources().getDrawable(R.drawable.nubosotormentalluviaescasa);
                break;
            case ("Poco nuboso"):
                x = getResources().getDrawable(R.drawable.poconuboso);
                break;
        }
        est.setImageDrawable(x);
    }
}
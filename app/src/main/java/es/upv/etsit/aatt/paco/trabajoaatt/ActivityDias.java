package es.upv.etsit.aatt.paco.trabajoaatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.net.ssl.HttpsURLConnection;

public class ActivityDias extends AppCompatActivity {

    private static final String TAG = "API_REST";

    private String id_M, mun, prov, id_Pro;

    private TextView tv_lugar;
    private TextView tvLun, tempmaxLun, tempminLun, tvMar, tempmaxMar, tempminMar, tvMie, tempmaxMie, tempminMie, tvJue, tempmaxJue, tempminJue, tvVie, tempmaxVie, tempminVie, tvSab, tempmaxSab, tempminSab, tvDom, tempmaxDom, tempminDom;
    private ImageView LunEstado, MarEstado, MieEstado, JueEstado, VieEstado, SabEstado, DomEstado;

    private int [] temp_max = new int [7];
    private int [] temp_min = new int [7];
    private int [] prob_precip = new int [7];
    private String [] dir_viento = new String [7];
    private int [] vel_viento = new int [7];
    private String [] est_cielo = new String [7];
    private String [] fecha = new String [7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dias);

        //Establecemos el logo en la action Bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_foreground);

        tv_lugar = (TextView)findViewById(R.id.tvlugar);

        tvLun = (TextView)findViewById(R.id.tvLun);
        tvMar = (TextView)findViewById(R.id.tvMar);
        tvMie = (TextView)findViewById(R.id.tvMie);
        tvJue = (TextView)findViewById(R.id.tvJue);
        tvVie = (TextView)findViewById(R.id.tvVie);
        tvSab = (TextView)findViewById(R.id.tvSab);
        tvDom = (TextView)findViewById(R.id.tvDom);

        tempmaxLun = (TextView)findViewById(R.id.tempmaxLun);
        tempmaxMar = (TextView)findViewById(R.id.tempmaxMar);
        tempmaxMie = (TextView)findViewById(R.id.tempmaxMie);
        tempmaxJue = (TextView)findViewById(R.id.tempmaxJue);
        tempmaxVie = (TextView)findViewById(R.id.tempmaxVie);
        tempmaxSab = (TextView)findViewById(R.id.tempmaxSab);
        tempmaxDom = (TextView)findViewById(R.id.tempmaxDom);

        tempminLun = (TextView)findViewById(R.id.tempminLun);
        tempminMar = (TextView)findViewById(R.id.tempminMar);
        tempminMie = (TextView)findViewById(R.id.tempminMie);
        tempminJue = (TextView)findViewById(R.id.tempminJue);
        tempminVie = (TextView)findViewById(R.id.tempminVie);
        tempminSab = (TextView)findViewById(R.id.tempminSab);
        tempminDom = (TextView)findViewById(R.id.tempminDom);

        LunEstado = (ImageView)findViewById(R.id.LunEstado);
        MarEstado = (ImageView)findViewById(R.id.MarEstado);
        MieEstado = (ImageView)findViewById(R.id.MieEstado);
        JueEstado = (ImageView)findViewById(R.id.JueEstado);
        VieEstado = (ImageView)findViewById(R.id.VieEstado);
        SabEstado = (ImageView)findViewById(R.id.SabEstado);
        DomEstado = (ImageView)findViewById(R.id.DomEstado);

        id_M = getIntent().getStringExtra("Id_Municipio");

        id_Pro = getIntent().getStringExtra("Id_provincia");

        mun = getIntent().getStringExtra("Municipio");

        prov = getIntent().getStringExtra("Provincia");

        String key = "?api_key=" + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4YmVyY29yQHRlbGVjby51cHYuZXMiLCJqdGkiOiJmNGFkMzE4Ni1kNDE1LTQ3NTAtYmMwNS02MTAzOWIxMzU0YmEiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTY1Mjk0OTUzMywidXNlcklkIjoiZjRhZDMxODYtZDQxNS00NzUwLWJjMDUtNjEwMzliMTM1NGJhIiwicm9sZSI6IiJ9.3ZFd3fbmDSo6SFyXiU4RdwsrIAndmemBXZBHeoya654";
        String url = "https://opendata.aemet.es/opendata/api/prediccion/especifica/municipio/diaria/" + id_M + key;
        System.out.println("la clave que llega " + url);

        ServiciosWebEncadenados3 servicioWeb3 = new ServiciosWebEncadenados3(url);
        servicioWeb3.start();
    }

    class ServiciosWebEncadenados3 extends Thread {

        String url_inicial;

        // constructor
        ServiciosWebEncadenados3(String url_inicial) {
            this.url_inicial = url_inicial;
        }

        // tarea a ejecutar en hilo paralelo e independiente

        @Override
        public void run() {
            // Gestiónese oportunamente las excepciones
            try {
                // Primera peticion

                String respuesta = API_REST(url_inicial);

                JSONObject objeto = new JSONObject(respuesta);
                String segunda_url = objeto.getString("datos");


                //Segunda peticion
                final String respuesta2 = API_REST(segunda_url);
                System.out.println("respuesta" + respuesta);
                System.out.println("respuesta2" + respuesta2);

                // Impresión de resultados en el hilo de la UI (User Interface thread): runOnUiThread
                runOnUiThread(() -> Mostrar(respuesta2));


            } catch (JSONException e) {
                Toast.makeText(ActivityDias.this, "Se ha produciodo un ERROR 2", Toast.LENGTH_LONG).show();
            }
        } // run
    }

    //Mostramos por pantalla toda la info a partir del JSON
    public void Mostrar(String respuesta2) {
        try {
            JSONArray array = new JSONArray(respuesta2);

            for(int i =0;i<7;i++){
                temp_max[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONObject("temperatura").getInt("maxima");

                temp_min[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONObject("temperatura").getInt("minima");

                prob_precip[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONArray("probPrecipitacion").getJSONObject(0).getInt("value");

                dir_viento[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONArray("viento").getJSONObject(0).getString("direccion");

                vel_viento[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONArray("viento").getJSONObject(0).getInt("velocidad");

                est_cielo[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONArray("estadoCielo").getJSONObject(0).getString("descripcion");           //fecha = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getString(fecha);

                fecha[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getString("fecha");
            }

            est_cielo[0] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("estadoCielo").getJSONObject(5).getString("descripcion");
            dir_viento[0] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("viento").getJSONObject(5).getString("direccion");

            Imagenes(est_cielo[0], "LunEstado");
            Imagenes(est_cielo[1], "MarEstado");
            Imagenes(est_cielo[2], "MieEstado");
            Imagenes(est_cielo[3], "JueEstado");
            Imagenes(est_cielo[4], "VieEstado");
            Imagenes(est_cielo[5], "SabEstado");
            Imagenes(est_cielo[6], "DomEstado");


            Temperaturas(temp_max[0], temp_min[0], fecha[0], "LunEstado");
            Temperaturas(temp_max[1], temp_min[1], fecha[1], "MarEstado");
            Temperaturas(temp_max[2], temp_min[2], fecha[2], "MieEstado");
            Temperaturas(temp_max[3], temp_min[3], fecha[3], "JueEstado");
            Temperaturas(temp_max[4], temp_min[4], fecha[4], "VieEstado");
            Temperaturas(temp_max[5], temp_min[5], fecha[5], "SabEstado");
            Temperaturas(temp_max[6], temp_min[6], fecha[6], "DomEstado");

            tv_lugar.setText("En " + mun + ", " + prov + " a " + DiaSemana(fecha[0].substring(0, 10)) + " " + fecha[0].substring(8, 10)  + " de " + MesAño(fecha[0].substring(0,10)));


        } catch (JSONException e) {
            Toast.makeText(ActivityDias.this, "Se ha produciodo un ERROR ", Toast.LENGTH_LONG).show();
        }
    }

    //Establecemos las imagenes segun el estado del cielo
    public void Imagenes(String estado_cielo, String dias){
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

        if(dias.equals("LunEstado")){
            LunEstado.setImageDrawable(x);

        } else if(dias.equals("MarEstado")) {
            MarEstado.setImageDrawable(x);

        } else if(dias.equals("MieEstado")) {
            MieEstado.setImageDrawable(x);

        } else if(dias.equals("JueEstado")) {
            JueEstado.setImageDrawable(x);

        } else if(dias.equals("VieEstado")) {
            VieEstado.setImageDrawable(x);

        } else if(dias.equals("SabEstado")) {
            SabEstado.setImageDrawable(x);

        } else if(dias.equals("DomEstado")) {
            DomEstado.setImageDrawable(x);
        }
    }

    //Establece las temperaturas en el dia correspondiente
    public void Temperaturas(int tempmax, int tempmin, String fecha, String dias){

        if(dias.equals("LunEstado")){
            tempmaxLun.setText(tempmax + "ºC");
            tempminLun.setText(tempmin + "ºC");
            tvLun.setText("Hoy");
        }
        else if(dias.equals("MarEstado")) {
            tempmaxMar.setText(tempmax + "ºC");
            tempminMar.setText(tempmin + "ºC");
            String dia = DiaSemana(fecha.substring(0, 10));
            tvMar.setText(dia + " " + fecha.substring(8, 10));
        }
        else if(dias.equals("MieEstado")) {
            tempmaxMie.setText(tempmax + "ºC");
            tempminMie.setText(tempmin + "ºC");
            String dia = DiaSemana(fecha.substring(0, 10));
            tvMie.setText(dia + " " + fecha.substring(8, 10));
        }
        else if(dias.equals("JueEstado")) {
            tempmaxJue.setText(tempmax + "ºC");
            tempminJue.setText(tempmin + "ºC");
            String dia = DiaSemana(fecha.substring(0, 10));
            tvJue.setText(dia + " " + fecha.substring(8, 10));
        }
        else if(dias.equals("VieEstado")) {
            tempmaxVie.setText(tempmax + "ºC");
            tempminVie.setText(tempmin + "ºC");
            String dia = DiaSemana(fecha.substring(0, 10));
            tvVie.setText(dia + " " + fecha.substring(8, 10));
        }
        else if(dias.equals("SabEstado")) {
            tempmaxSab.setText(tempmax + "ºC");
            tempminSab.setText(tempmin + "ºC");
            String dia = DiaSemana(fecha.substring(0, 10));
            tvSab.setText(dia + " " + fecha.substring(8, 10));
        }
        else if(dias.equals("DomEstado")) {
            tempmaxDom.setText(tempmax + "ºC");
            tempminDom.setText(tempmin + "ºC");
            String dia = DiaSemana(fecha.substring(0, 10));
            tvDom.setText(dia + " " + fecha.substring(8, 10));
        }
    }


    //Obtenemos a partir de la fecha que dia de la semana es
    public String DiaSemana(String fecha){
        String Valor_dia = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = null;

        try{
            fechaActual = df.parse(fecha);
        }catch(ParseException e){
            Toast.makeText(this, "NO se ha podido sacar la fecha", Toast.LENGTH_SHORT).show();
        }
        GregorianCalendar fechaCalendaio = new GregorianCalendar();
        fechaCalendaio.setTime(fechaActual);
        int diaSemana = fechaCalendaio.get(Calendar.DAY_OF_WEEK);

        if(diaSemana == 1){
            Valor_dia = "Domingo";
        } else if(diaSemana == 2){
            Valor_dia = "Lunes";
        } else if(diaSemana == 3){
            Valor_dia = "Martes";
        } else if(diaSemana == 4){
            Valor_dia = "Miercoles";
        } else if(diaSemana == 5) {
            Valor_dia = "Jueves";
        } else if(diaSemana == 6) {
            Valor_dia = "Viernes";
        } else if(diaSemana == 7) {
            Valor_dia = "Sábado";
        }
        return Valor_dia;
    }

    //Obtenemos a partir de la fecha que mes del año es
    public String MesAño(String fecha){
        String Valor_mes = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = null;

        try{
            fechaActual = df.parse(fecha);
        }catch(ParseException e){
            Toast.makeText(this, "NO se ha podido sacar la fecha", Toast.LENGTH_SHORT).show();
        }
        GregorianCalendar fechaCalendaio = new GregorianCalendar();
        fechaCalendaio.setTime(fechaActual);
        int mesAño = fechaCalendaio.get(Calendar.MONTH);

        if(mesAño == 0){
            Valor_mes = "Enero";
        } else if(mesAño == 1){
            Valor_mes = "Febrero";
        } else if(mesAño == 2){
            Valor_mes = "Marzo";
        } else if(mesAño == 3){
            Valor_mes = "Abril";
        } else if(mesAño == 4) {
            Valor_mes = "Mayo";
        } else if(mesAño == 5) {
            Valor_mes = "Junio";
        } else if(mesAño == 6) {
            Valor_mes = "Julio";
        } else if(mesAño == 7){
            Valor_mes = "Agosto";
        } else if(mesAño == 8){
            Valor_mes = "Septiembre";
        } else if(mesAño == 9) {
            Valor_mes = "Octubre";
        } else if(mesAño == 10) {
            Valor_mes = "Noviembre";
        } else if(mesAño == 11) {
            Valor_mes = "Diciembre";
        }
        return Valor_mes;
    }

    //Boton 0
    public void Mostrar0(View view) {
        Intent siguiente = new Intent(ActivityDias.this, ActivityFin.class);
        siguiente.putExtra("Provincia", prov);
        siguiente.putExtra("Nombre", mun);
        siguiente.putExtra("tempMax", toString().valueOf(temp_max[0]));
        siguiente.putExtra("tempMin", toString().valueOf(temp_min[0]));
        siguiente.putExtra("probPre", toString().valueOf(prob_precip[0]));
        siguiente.putExtra("dirViento", dir_viento[0]);
        siguiente.putExtra("velViento", toString().valueOf(vel_viento[0]));
        siguiente.putExtra("estCielo", est_cielo[0]);
        siguiente.putExtra("Id_provincia", id_Pro);
        startActivity(siguiente);
    }

    //Boton 1
    public void Mostrar1 (View view1){
        Intent siguiente = new Intent(ActivityDias.this, ActivityFin.class);
        siguiente.putExtra("Provincia",prov);
        siguiente.putExtra("Nombre",mun);
        siguiente.putExtra("tempMax",toString().valueOf(temp_max[1]));
        siguiente.putExtra("tempMin",toString().valueOf(temp_min[1]));
        siguiente.putExtra("probPre",toString().valueOf(prob_precip[1]));
        siguiente.putExtra("dirViento",dir_viento[1]);
        siguiente.putExtra("velViento",toString().valueOf(vel_viento[1]));
        siguiente.putExtra("estCielo",est_cielo[1]);
        siguiente.putExtra("Id_provincia", id_Pro);
        startActivity(siguiente);
    }

    //Boton 2
    public void Mostrar2 (View view2){
        Intent siguiente = new Intent(ActivityDias.this, ActivityFin.class);
        siguiente.putExtra("Provincia",prov);
        siguiente.putExtra("Nombre",mun);
        siguiente.putExtra("tempMax",toString().valueOf(temp_max[2]));
        siguiente.putExtra("tempMin",toString().valueOf(temp_min[2]));
        siguiente.putExtra("probPre",toString().valueOf(prob_precip[2]));
        siguiente.putExtra("dirViento",dir_viento[2]);
        siguiente.putExtra("velViento",toString().valueOf(vel_viento[2]));
        siguiente.putExtra("estCielo",est_cielo[2]);
        siguiente.putExtra("Id_provincia", id_Pro);
        startActivity(siguiente);
    }

    //Boton 3
    public void Mostrar3 (View view3){
        Intent siguiente = new Intent(ActivityDias.this, ActivityFin.class);
        siguiente.putExtra("Provincia",prov);
        siguiente.putExtra("Nombre",mun);
        siguiente.putExtra("tempMax",toString().valueOf(temp_max[3]));
        siguiente.putExtra("tempMin",toString().valueOf(temp_min[3]));
        siguiente.putExtra("probPre",toString().valueOf(prob_precip[3]));
        siguiente.putExtra("dirViento",dir_viento[3]);
        siguiente.putExtra("velViento",toString().valueOf(vel_viento[3]));
        siguiente.putExtra("estCielo",est_cielo[3]);
        siguiente.putExtra("Id_provincia", id_Pro);
        startActivity(siguiente);
    }

    //Boton 4
    public void Mostrar4 (View view4){
        Intent siguiente = new Intent(ActivityDias.this, ActivityFin.class);
        siguiente.putExtra("Provincia",prov);
        siguiente.putExtra("Nombre",mun);
        siguiente.putExtra("tempMax",toString().valueOf(temp_max[4]));
        siguiente.putExtra("tempMin",toString().valueOf(temp_min[4]));
        siguiente.putExtra("probPre",toString().valueOf(prob_precip[4]));
        siguiente.putExtra("dirViento",dir_viento[4]);
        siguiente.putExtra("velViento",toString().valueOf(vel_viento[4]));
        siguiente.putExtra("estCielo",est_cielo[4]);
        siguiente.putExtra("Id_provincia", id_Pro);
        startActivity(siguiente);
    }

    //Boton 5
    public void Mostrar5 (View view5){
        Intent siguiente = new Intent(ActivityDias.this, ActivityFin.class);
        siguiente.putExtra("Provincia",prov);
        siguiente.putExtra("Nombre",mun);
        siguiente.putExtra("tempMax",toString().valueOf(temp_max[5]));
        siguiente.putExtra("tempMin",toString().valueOf(temp_min[5]));
        siguiente.putExtra("probPre",toString().valueOf(prob_precip[5]));
        siguiente.putExtra("dirViento",dir_viento[5]);
        siguiente.putExtra("velViento",toString().valueOf(vel_viento[5]));
        siguiente.putExtra("estCielo",est_cielo[5]);
        siguiente.putExtra("Id_provincia", id_Pro);
        startActivity(siguiente);
    }

    //Boton 6
    public void Mostrar6 (View view6){
        Intent siguiente = new Intent(ActivityDias.this, ActivityFin.class);
        siguiente.putExtra("Provincia",prov);
        siguiente.putExtra("Nombre",mun);
        siguiente.putExtra("tempMax",toString().valueOf(temp_max[6]));
        siguiente.putExtra("tempMin",toString().valueOf(temp_min[6]));
        siguiente.putExtra("probPre",toString().valueOf(prob_precip[6]));
        siguiente.putExtra("dirViento",dir_viento[6]);
        siguiente.putExtra("velViento",toString().valueOf(vel_viento[6]));
        siguiente.putExtra("estCielo",est_cielo[6]);
        siguiente.putExtra("Id_provincia", id_Pro);
        startActivity(siguiente);
    }


    public String API_REST(String uri) {

        StringBuffer response = null;

        try {
            URL url = new URL(uri);
            Log.d(TAG, "URL: " + uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Detalles de HTTP
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Codigo de respuesta: " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        // Presumiblemente, la codificación de la respuesta es ISO-8859-15
                        new InputStreamReader(conn.getInputStream(), "ISO-8859-15"));
                String output;
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
            } else {
                Log.d(TAG, "responseCode: " + responseCode);
                return null; // retorna null anticipadamente si hay algun problema
            }

        } catch (Exception e) { // Posibles excepciones: MalformedURLException, IOException y ProtocolException
            e.printStackTrace();
            Log.d(TAG, "Error conexión HTTP2:" + e.toString());
            return null;
        }

        return new String(response); // de StringBuffer -response- pasamos a String
    } // API_REST
}



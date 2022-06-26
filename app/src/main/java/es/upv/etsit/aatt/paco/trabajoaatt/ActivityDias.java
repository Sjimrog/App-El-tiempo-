package es.upv.etsit.aatt.paco.trabajoaatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ActivityDias extends AppCompatActivity {

    private static final String TAG = "API_REST";

    public String id_M, mun, prov;

    private TextView tv_lugar;

    public int [] temp_max = new int [7];
    public int [] temp_min = new int [7];
    public int [] prob_precip = new int [7];
    public String [] dir_viento = new String [7];
    public int [] vel_viento = new int [7];
    public String [] est_cielo = new String [7];
    public String [] fecha = new String [7];

    private ListView listview;
    private ArrayList<String> dias;
    //public static String [] nombreDias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dias);

        tv_lugar = (TextView)findViewById(R.id.tvlugar);
        listview = (ListView)findViewById(R.id.ListView);

        id_M = getIntent().getStringExtra("Id_Municipio");

        mun = getIntent().getStringExtra("Municipio");
        System.out.println("ultima" + mun);
        prov = getIntent().getStringExtra("Provincia");
        tv_lugar.setText("Pueblo: " + mun+ " de la provincia: " + prov);
        String key = "?api_key=" + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4YmVyY29yQHRlbGVjby51cHYuZXMiLCJqdGkiOiJmNGFkMzE4Ni1kNDE1LTQ3NTAtYmMwNS02MTAzOWIxMzU0YmEiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTY1Mjk0OTUzMywidXNlcklkIjoiZjRhZDMxODYtZDQxNS00NzUwLWJjMDUtNjEwMzliMTM1NGJhIiwicm9sZSI6IiJ9.3ZFd3fbmDSo6SFyXiU4RdwsrIAndmemBXZBHeoya654";
        String url = "https://opendata.aemet.es/opendata/api/prediccion/especifica/municipio/diaria/" + id_M + key;
        System.out.println("la clave que llega " + url);

        ServiciosWebEncadenados3 servicioWeb3 = new ServiciosWebEncadenados3(url);
        servicioWeb3.start();

        /**dias = new ArrayList<String>();
        dias.add("Lunes");
        dias.add("Martes");
        dias.add("Miercoles");
        dias.add("Jueves");
        dias.add("Viernes");
        dias.add("Sábado");
        dias.add("Domingo");

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout., dias);
        listview.setAdapter(myAdapter);*/

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
                System.out.println("segunda url" + segunda_url);

                //System.out.println(respuesta2);
                //Segunda peticion

                final String respuesta2 = API_REST(segunda_url);
                System.out.println("respuesta" + respuesta);
                System.out.println("respuesta2" + respuesta2);
                runOnUiThread(() -> Mostrar(respuesta2));


            } catch (JSONException e) {
                Toast.makeText(ActivityDias.this, "Se ha produciodo un ERROR 2", Toast.LENGTH_LONG).show();
            }
        } // run


        public void Mostrar(String respuesta2) {
            try {
                tv_lugar.setText(mun + ", " + prov);

                JSONArray array = new JSONArray(respuesta2);

                for(int i =0;i<50;i++){

                    temp_max[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONObject("temperatura").getInt("maxima");
                    System.out.println("TMax"+ i+" "+temp_max[i]);

                    temp_min[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONObject("temperatura").getInt("minima");
                    System.out.println("TMin"+ i+" "+temp_min[i]);

                   prob_precip[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONArray("probPrecipitacion").getJSONObject(0).getInt("value");
                    System.out.println("TMprob"+ i+" "+prob_precip[i]);

                    dir_viento[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONArray("viento").getJSONObject(0).getString("direccion");
                    System.out.println("dir"+ i+" "+dir_viento[i]);

                    vel_viento[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONArray("viento").getJSONObject(0).getInt("velocidad");
                    System.out.println("velo"+ i+" "+vel_viento[i]);

                    est_cielo[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getJSONArray("estadoCielo").getJSONObject(0).getString("descripcion");           //fecha = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getString(fecha);
                    System.out.println("Estado"+ i+" "+est_cielo[i]);

                    fecha[i] = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(i).getString("fecha");
                    System.out.println("fecha" + i +" "+fecha[i]);
                    //i += 1;
                }

                //for(int i = 0; i <= 6; i++){



            } catch (JSONException e) {
                Toast.makeText(ActivityDias.this, "Se ha produciodo un ERROR ", Toast.LENGTH_LONG).show();
            }
        }

        //Boton 0
        public void Mostrar0 (View view0) {
            Intent siguiente = new Intent(ActivityDias.this, ActivityFin.class);
            siguiente.putExtra("Provincia", prov);
            siguiente.putExtra("Nombre", mun);
            siguiente.putExtra("tempMax", toString().valueOf(temp_max[0]));
            siguiente.putExtra("tempMin", toString().valueOf(temp_min[0]));
            siguiente.putExtra("probPre", toString().valueOf(prob_precip[0]));
            siguiente.putExtra("dirViento", dir_viento[0]);
            siguiente.putExtra("velViento", toString().valueOf(vel_viento[0]));
            siguiente.putExtra("estCielo", est_cielo[0]);
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
}


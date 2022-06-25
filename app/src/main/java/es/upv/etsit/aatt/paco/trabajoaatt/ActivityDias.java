package es.upv.etsit.aatt.paco.trabajoaatt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

    public int [] temp_max = new int [6];
    public int [] temp_min = new int [6];
    public int [] prob_precip = new int [6];
    public String [] dir_viento = new String [6];
    public int [] vel_viento = new int [6];
    public String [] est_cielo = new String [6];
    public String [] fecha = new String [6];

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
        String key = "?api_key=" + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4YmVyY29yQHRlbGVjby51cHYuZXMiLCJqdGkiOiJmNGFkMzE4Ni1kNDE1LTQ3NTAtYmMwNS02MTAzOWIxMzU0YmEiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTY1Mjk0OTUzMywidXNlcklkIjoiZjRhZDMxODYtZDQxNS00NzUwLWJjMDUtNjEwMzliMTM1NGJhIiwicm9sZSI6IiJ9.3ZFd3fbmDSo6SFyXiU4RdwsrIAndmemBXZBHeoya654";
        String url = "https://opendata.aemet.es/opendata/api/prediccion/especifica/municipio/diaria/" + id_M + key;
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

                final String respuesta = API_REST(url_inicial);

                JSONObject objeto = new JSONObject(respuesta);
                String segunda_url = objeto.getString("datos");
                System.out.println(segunda_url);

                //System.out.println(respuesta2);
                //Segunda peticion

                final String respuesta2 = API_REST(segunda_url);

                runOnUiThread(() -> Mostrar(respuesta2));


            } catch (JSONException e) {
                Toast.makeText(ActivityDias.this, "Se ha produciodo un ERROR 2", Toast.LENGTH_LONG).show();
            }
        } // run


        public void Mostrar(String respuesta2) {
            try {
                tv_lugar.setText(mun + ", " + prov);

                JSONArray array = new JSONArray(respuesta2);

                for(int i = 0; i <= 6; i++) {
                    temp_max[i] = array.getJSONObject(i).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(1).getJSONObject("temperatura").getInt("maxima");
                    temp_min[i] = array.getJSONObject(i).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(1).getJSONObject("temperatura").getInt("minima");

                    prob_precip[i] = array.getJSONObject(i).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(1).getJSONArray("probPrecipitacion").getJSONObject(5).getInt("value");

                    dir_viento[i] = array.getJSONObject(i).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(1).getJSONArray("viento").getJSONObject(5).getString("direccion");
                    vel_viento[i] = array.getJSONObject(i).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(1).getJSONArray("viento").getJSONObject(5).getInt("velocidad");

                    est_cielo[i] = array.getJSONObject(i).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(1).getJSONArray("estadoCielo").getJSONObject(5).getString("descripcion");           //fecha = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getString(fecha);
                    fecha[i] = array.getJSONObject(i).getString("fecha");
                }

                for(int i = 0; i <= 6; i++){

                }

            } catch (JSONException e) {
                Toast.makeText(ActivityDias.this, "Se ha produciodo un ERROR ", Toast.LENGTH_LONG).show();
            }
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
                Log.d(TAG, "Error conexión HTTP:" + e.toString());
                return null;
            }

            return new String(response); // de StringBuffer -response- pasamos a String

        } // API_REST
    }
}


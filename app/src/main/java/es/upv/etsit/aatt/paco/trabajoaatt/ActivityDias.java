package es.upv.etsit.aatt.paco.trabajoaatt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ActivityDias extends AppCompatActivity {

    private static final String TAG = "API_REST";

    public String id_M;
    public String fecha;

    private TextView tv;
    public String pueblo, provincia, est_cielo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dias);

        tv = (TextView)findViewById(R.id.tvfecha);

        id_M = getIntent().getStringExtra("Municipio");
        String key = "?api_key=" + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4YmVyY29yQHRlbGVjby51cHYuZXMiLCJqdGkiOiJmNGFkMzE4Ni1kNDE1LTQ3NTAtYmMwNS02MTAzOWIxMzU0YmEiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTY1Mjk0OTUzMywidXNlcklkIjoiZjRhZDMxODYtZDQxNS00NzUwLWJjMDUtNjEwMzliMTM1NGJhIiwicm9sZSI6IiJ9.3ZFd3fbmDSo6SFyXiU4RdwsrIAndmemBXZBHeoya654";
        String url = "https://opendata.aemet.es/opendata/api/prediccion/especifica/municipio/diaria/" + id_M + key;
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
                JSONArray array = new JSONArray(respuesta2);
                pueblo = array.getJSONObject(0).getString("nombre");
                provincia = array.getJSONObject(0).getString("provincia");
                est_cielo = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("estadoCielo").getJSONObject(5).getString("descripcion");

                //fecha = array.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getString(fecha);

                System.out.println(est_cielo);
                tv.setText(pueblo);

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


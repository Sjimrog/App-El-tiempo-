package es.upv.etsit.aatt.paco.trabajoaatt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ActivityDias extends AppCompatActivity {

    private static final String TAG = "API_REST";

    private String id_M;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dias);

        id_M = getIntent().getStringExtra("Municipio");
        String key = "?api_key=" + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4YmVyY29yQHRlbGVjby51cHYuZXMiLCJqdGkiOiJmNGFkMzE4Ni1kNDE1LTQ3NTAtYmMwNS02MTAzOWIxMzU0YmEiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTY1Mjk0OTUzMywidXNlcklkIjoiZjRhZDMxODYtZDQxNS00NzUwLWJjMDUtNjEwMzliMTM1NGJhIiwicm9sZSI6IiJ9.3ZFd3fbmDSo6SFyXiU4RdwsrIAndmemBXZBHeoya654";
        String url = "https://opendata.aemet.es/opendata/api/prediccion/especifica/municipio/diaria/" + id_M + key;
        ActivityMun.ServiciosWebEncadenados2 servicioWeb2 = new ActivityMun.ServiciosWebEncadenados2(url);
        servicioWeb2.start();
    }

    class ServiciosWebEncadenados3 extends Thread {

        String url_inicial;

        // constructor
        ServiciosWebEncadenados3(String url_inicial) {
            this.url_inicial = url_inicial;
        }

        // tarea a ejecutar en hilo paralelo e independiente

        @Override public void run() {
            // Gestiónese oportunamente las excepciones
            try {
                // Primera peticion

                final String respuesta2 = API_REST(url_inicial);
                System.out.println(respuesta2);
                runOnUiThread(() -> ListMunicipio(respuesta2));


            } catch (Exception e) {
                Toast.makeText(ActivityMun.this, "Se ha produciodo un error ", Toast.LENGTH_LONG).show();
            }
        } // run



        public void








    public String API_REST(String uri){

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
                        new InputStreamReader(conn.getInputStream() , "ISO-8859-15" ));
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


        } catch(Exception e) { // Posibles excepciones: MalformedURLException, IOException y ProtocolException
            e.printStackTrace();
            Log.d(TAG, "Error conexión HTTP:" + e.toString());
            return null;
        }

        return new String(response); // de StringBuffer -response- pasamos a String

    } // API_REST
}


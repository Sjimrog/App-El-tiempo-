package es.upv.etsit.aatt.paco.trabajoaatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ActivityMun extends AppCompatActivity {


    private static final String TAG = "API_REST";

    ArrayList<String> lista_mun = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter_mun;
    Spinner spinner_mun;

    private String id_mun, id_P;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mun);

        id_P= getIntent().getStringExtra("Provincia");
        String urlMun = "https://raw.githubusercontent.com/IagoLast/pselect/master/data/municipios.json";
        ActivityMun.ServiciosWebEncadenados2 servicioWeb2 = new ServiciosWebEncadenados2(urlMun);
        servicioWeb2.start();
    }

    class ServiciosWebEncadenados2 extends Thread {

        String url_inicial;

        // constructor
        ServiciosWebEncadenados2(String url_inicial) {
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

        public void ListMunicipio(String municipio) {
            Log.i(TAG, "informacion");

            spinner_mun = (Spinner) findViewById(R.id.spinner_mun);

            int i = 0;
            try {
                JSONArray arr2 = new JSONArray(municipio);
                do {
                    String num_mun = arr2.getJSONObject(i).getString("id");
                    System.out.println(num_mun);
                    if(num_mun.equals(null)){
                        i += 1;
                    }else {
                        System.out.println(num_mun.substring(0, 2));
                        System.out.println(id_P);
                        if(id_P.equals(num_mun.substring(0, 2))) {
                            String prov = arr2.getJSONObject(i).getString("nm");
                            System.out.println(prov);
                            lista_mun.add(prov);
                            i += 1;
                        } else {
                            i += 1;
                        }
                    }

                } while (i < arr2.length());

                arrayAdapter_mun = new ArrayAdapter<String>(ActivityMun.this, android.R.layout.simple_spinner_item, lista_mun);
                spinner_mun.setAdapter(arrayAdapter_mun);


                //Seleccionamos la id del municipio selecionado
                spinner_mun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void  onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            id_mun = arr2.getJSONObject(position).getString("id");
                            System.out.println(id_mun);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        Toast.makeText(ActivityMun.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
                    }
                });


            } catch (JSONException e) {
                Toast.makeText(ActivityMun.this, "error", Toast.LENGTH_LONG).show();
            }
        }

        //Boton Next
    public void Next (View view){
        Intent siguiente = new Intent(ActivityMun.this, ActivityDias.class);
        siguiente.putExtra("Municipio", id_mun);
        startActivity(siguiente);
    }

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
}
package es.upv.etsit.aatt.paco.trabajoaatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import es.upv.etsit.aatt.paco.trabajoaatt.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "API_REST";

    ArrayList<String> lista_Prov = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter_Prov;

    Spinner spinner_prov;


    private String id_prov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String urlProv = "https://raw.githubusercontent.com/IagoLast/pselect/master/data/provincias.json";
        ServiciosWebEncadenados servicioWeb = new ServiciosWebEncadenados(urlProv);
        servicioWeb.start();
        //System.out.println(id_mun);

    }
    //Provincias
    class ServiciosWebEncadenados extends Thread {

        String url_inicial;

        // constructor
        ServiciosWebEncadenados(String url_inicial) {
            this.url_inicial = url_inicial;
        }

        // tarea a ejecutar en hilo paralelo e independiente

        @Override public void run() {
            // Gestiónese oportunamente las excepciones
            try {
                // Primera peticion
                final String respuesta = API_REST(url_inicial);
                System.out.println(respuesta);
                // Impresión de resultados en el hilo de la UI (User Interface thread): runOnUiThread
                runOnUiThread(() -> ListProvincias(respuesta));



            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Se ha produciodo un error ", Toast.LENGTH_LONG).show();
            }
        } // run

    } // ServiciosWebEncadenados

    public void ListProvincias(String provincias) {
        Log.i(TAG, "informacion");

        spinner_prov = (Spinner) findViewById(R.id.spiner_prov);

        int i = 0;
        try {
            JSONArray arr = new JSONArray(provincias);
            do {
                String prov = arr.getJSONObject(i).getString("nm");
                //System.out.println(prov);
                lista_Prov.add(prov);
                i += 1;
            } while (i < arr.length());

            arrayAdapter_Prov = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_Prov);
            spinner_prov.setAdapter(arrayAdapter_Prov);



            //Seleccionamos la id de la provincia selecionada
            spinner_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void  onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        id_prov = arr.getJSONObject(position).getString("id");
                        System.out.println(id_prov);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    Toast.makeText(MainActivity.this, "NothingSelected", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (JSONException e) {
            Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
        }
    }
    //Municipios

    //Boton Next
    public void Next (View view){
        Intent siguiente = new Intent(this, ActivityMun.class);
        siguiente.putExtra("Provincia",id_prov);
        startActivity(siguiente);
    }

    //Peticion
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
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

    private String id_prov, nm_prov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Establecemos el logo en la action Bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_foreground);

        String urlProv = "https://raw.githubusercontent.com/IagoLast/pselect/master/data/provincias.json";
        ServiciosWebEncadenados servicioWeb = new ServiciosWebEncadenados(urlProv);
        servicioWeb.start();

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
                // Impresión de resultados en el hilo de la UI (User Interface thread): runOnUiThread
                runOnUiThread(() -> ListProvincias(respuesta));

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Se ha produciodo un ERROR ", Toast.LENGTH_LONG).show();
            }
        } // run

    } // ServiciosWebEncadenados

    //Esta parte del codigo sirve para obtener la lista con la que haremos el spinner
    public void ListProvincias(String provincias) {
        Log.i(TAG, "informacion");

        spinner_prov = (Spinner) findViewById(R.id.spiner_prov);

        int i = 0;
        try {    /**Con el do while nos recorremos todo el Json para sacar los nombres de las provincias y añadirlas a la lista la cual meteremos en el spinner*/
            JSONArray arr = new JSONArray(provincias);
            do {
                String prov = arr.getJSONObject(i).getString("nm");
                lista_Prov.add(prov);
                i += 1;
            } while (i < arr.length());

            arrayAdapter_Prov = new ArrayAdapter<String>(this, R.layout.spinner_item_info, lista_Prov);
            spinner_prov.setAdapter(arrayAdapter_Prov);



            //Seleccionamos la id de la provincia selecionada
            spinner_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override       //Una vez selecionado un elemento del spinner obtenemos su nombre y la id
                public void  onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        id_prov = arr.getJSONObject(position).getString("id");
                        nm_prov = arr.getJSONObject(position).getString("nm");

                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Se ha producido un error", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    Toast.makeText(MainActivity.this, "NothingSelected", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (JSONException e) {
            Toast.makeText(MainActivity.this, "Se ha producido un error", Toast.LENGTH_LONG).show();
        }
    }



    //Boton SELECCIONAR
    public void Next (View view){
        Intent siguiente = new Intent(this, ActivityMun.class);
        siguiente.putExtra("Provincia",id_prov);
        siguiente.putExtra("Nombre",nm_prov);
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
                        // Presumiblemente, la codificación de la respuesta es utf-8
                        new InputStreamReader(conn.getInputStream() , "utf-8" ));//Hemos puesto el utf-8 para poder leer las tildes y las letras típicas de idiomas
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
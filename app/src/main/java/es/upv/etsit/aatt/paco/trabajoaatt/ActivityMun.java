package es.upv.etsit.aatt.paco.trabajoaatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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

    private String id_mun, id_P, nm_P, mun, provinciaElegida, idProvinciaElegida;
    private TextView tv_nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mun);

        //Establecemos el logo en la action Bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_foreground);

        id_P = getIntent().getStringExtra("Provincia");
        nm_P = getIntent().getStringExtra("Nombre");

        provinciaElegida = getIntent().getStringExtra("ProvinciaElegida");
        idProvinciaElegida = getIntent().getStringExtra("IdProvinciaElegida");

        System.out.println("la id del inicia" + id_P);
        System.out.println("la del inicia" + nm_P);
        System.out.println("la del boton" + provinciaElegida);
        System.out.println("la id del boton" + idProvinciaElegida);

        //Elegimos segun si venimos de la ultima activity o de la primera
        if(provinciaElegida == null){
            id_P = id_P;
            nm_P = nm_P;
        }else {
            id_P = idProvinciaElegida;
            nm_P = provinciaElegida;
        }

        tv_nm = (TextView)findViewById(R.id.tv_nm);
        tv_nm.setText("Estás en la provincia: " + nm_P);

        String urlMun = "https://raw.githubusercontent.com/IagoLast/pselect/master/data/municipios.json";
        ServiciosWebEncadenados2 servicioWeb2 = new ServiciosWebEncadenados2(urlMun);
        servicioWeb2.start();
    }

    class ServiciosWebEncadenados2 extends Thread {

        String url_inicial;

        // constructor
        ServiciosWebEncadenados2(String url_inicial) {
            this.url_inicial = url_inicial;
        }

        // tarea a ejecutar en hilo paralelo e independiente
        @Override
        public void run() {
            // Gestiónese oportunamente las excepciones
            try {
                // Primera peticion
                final String respuesta2 = API_REST(url_inicial);

                // Impresión de resultados en el hilo de la UI (User Interface thread): runOnUiThread
                runOnUiThread(() -> ListMunicipio(respuesta2));

            } catch (Exception e) {
                Toast.makeText(ActivityMun.this, "Se ha producido un error", Toast.LENGTH_LONG).show();
            }
        } // run
    }

    //Obtener listado de municipios
    public void ListMunicipio(String municipio) {
        Log.i(TAG, "informacion");

        spinner_mun = (Spinner) findViewById(R.id.spinner_mun);

        int i = 0;
        try { //En este caso al tener la id de la provincia sabemos que la del municipio empieza por esta, por lo que comparamos los inicios y los que cuadran se añaden a la lista
            JSONArray arr2 = new JSONArray(municipio);
            do {
                String num_mun = arr2.getJSONObject(i).getString("id");

                if (num_mun.equals(null)) {
                    i += 1;
                } else {
                    if (id_P.equals(num_mun.substring(0, 2))) { //Comparación para añadir al spinner
                        String prov = arr2.getJSONObject(i).getString("nm");
                        lista_mun.add(prov);
                        i += 1;
                    } else {
                        i += 1;
                    }
                }

            } while (i < arr2.length());

            arrayAdapter_mun = new ArrayAdapter<String>(ActivityMun.this, R.layout.spinner_item_info, lista_mun);
            spinner_mun.setAdapter(arrayAdapter_mun);


            //Seleccionamos la id del municipio selecionado
            spinner_mun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        mun = lista_mun.get(position); //Con esto obtenemos el nombre del municipio obtenido

                        int i = 0;
                        do {
                            String municipio = arr2.getJSONObject(i).getString("nm");

                            if (municipio.equals(mun)) { //Vamos comparando todos los municipios con el seleccionado hasta que cuadre

                                id_mun = arr2.getJSONObject(i).getString("id"); //una vez cuadrado sacamos la id del municipio sabiendo su posición
                                break;
                            } else {
                                i += 1;
                            }

                        } while (i < arr2.length());

                    } catch (JSONException e) {
                        Toast.makeText(ActivityMun.this, "Se ha producido un error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    Toast.makeText(ActivityMun.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            Toast.makeText(ActivityMun.this, "Se ha producido un error", Toast.LENGTH_LONG).show();
        }
    }



    //Boton Next
    public void Next(View view){
        Intent dias = new Intent(ActivityMun.this, ActivityDias.class);
        dias.putExtra("Id_Municipio", id_mun);
        dias.putExtra("Provincia", nm_P);
        dias.putExtra("Municipio", mun);
        dias.putExtra("Id_provincia", id_P);
        System.out.println(id_P + " pasamos id a siguiente activity");
        startActivity(dias);
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
                        // Presumiblemente, la codificación de la respuesta es utf-8
                        new InputStreamReader(conn.getInputStream() , "utf-8" ));
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

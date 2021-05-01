package com.example.gsb_java;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Collections;

public class CreateCRActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapport);

        try {
            ApiRequest.makeRequest(this.getApplicationContext(), this,"praticien","Praticien/read.php", Request.Method.POST, GlobalContext.api, null, "erreurprat");
        } catch (ApiRequest.APICallbackException e) {
            e.printStackTrace();
            connectError();
        }
        try {
            ApiRequest.makeRequest(this.getApplicationContext(), this,"medicament","Medicament/read.php", Request.Method.POST, GlobalContext.api, null, "erreurmedic");
        } catch (ApiRequest.APICallbackException e) {
            e.printStackTrace();
            connectError();
        }
    }
    @APICallback(tag = "praticien")
    public void testCallback(JSONObject response) throws JSONException {
        GlobalContext.response = response;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);

        // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner praticien = (Spinner) findViewById(R.id.praticien);
        for(int i=0; i < response.getJSONArray("praticiens").length();i++){
            Log.v("Praticine", response.getJSONArray("praticiens").getJSONObject(i).getString("nom_praticien"));
            JSONObject userpraticien = response.getJSONArray("praticiens").getJSONObject(i);


            // (@resource) android.R.layout.simple_spinner_item:
            //   The resource ID for a layout file containing a TextView to use when instantiating views.
            //    (Layout for one ROW of Spinner)
            adapter.add(userpraticien.getString("nom_praticien")+" "+userpraticien.getString("prenom_praticien"));

            // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).


            }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        praticien.setAdapter(adapter);
        praticien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                Log.v("Position", String.valueOf(position));
                GlobalContext.id_praticien = GlobalContext.response.getJSONArray("praticiens").getJSONObject(position).getInt("id_praticien");
                Log.v("Id_praticien", String.valueOf(GlobalContext.id_praticien));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        }

    @APICallback(tag= "erreurprat")
    public void connectError() {
        //((TextView) findViewById(R.id.textView2)).setText(getString(R.string.errorConnect));
        Log.v("erreurprat", "Erreur");
    }
    @APICallback(tag = "medicament")
    public void testCallback1(JSONObject response) throws JSONException {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);

        // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner produit1 = (Spinner) findViewById(R.id.produit1);
        Spinner produit2 = (Spinner) findViewById(R.id.produit2);
        Log.v("taille", String.valueOf(response.getJSONArray("medicaments").length()));
        for(int i=0; i < response.getJSONArray("medicaments").length();i++){
            JSONObject medic = response.getJSONArray("medicaments").getJSONObject(i);


            // (@resource) android.R.layout.simple_spinner_item:
            //   The resource ID for a layout file containing a TextView to use when instantiating views.
            //    (Layout for one ROW of Spinner)
            adapter.add(medic.getString("nom_commercial"));

            // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).


        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        produit1.setAdapter(adapter);
        produit2.setAdapter(adapter);
        produit1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        produit2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @APICallback(tag= "erreurmedic")
    public void connectError1() {
        //((TextView) findViewById(R.id.textView2)).setText(getString(R.string.errorConnect));
        Log.v("erreurprat", "Erreur");
    }
    public void sendRapport(View view){
        EditText motif = findViewById(R.id.motif);
        // password visiteur
        EditText bilan = findViewById(R.id.bilan);
        String motif_str = motif.getText().toString();
        String bilan_str = bilan.getText().toString();
        try {
            ApiRequest.makeRequest(this.getApplicationContext(), this,"create_rapport","Rapport_visite/create.php?id_visiteur=1&id_praticien="+GlobalContext.id_praticien+"&bilan="+bilan_str+"&motif="+motif_str, Request.Method.POST, GlobalContext.api, null, "erreur_create_rapport");
        } catch (ApiRequest.APICallbackException e) {
            e.printStackTrace();
            connectError();
        }

    }
    @APICallback(tag = "create_rapport")
    public void testCallback3(JSONObject response) throws JSONException {
        Log.v("create_rapport", response.toString());
        setContentView(R.layout.rapport_success);

    }
    @APICallback(tag= "erreur_create_rapport")
    public void connectError3() {
        //((TextView) findViewById(R.id.textView2)).setText(getString(R.string.errorConnect));
        Log.v("errorConnect", "Erreur");
    }
    public void backMenu(View view){
        Intent intent = new Intent(CreateCRActivity.this, MenuActivity.class);
        startActivity(intent);
    }

}

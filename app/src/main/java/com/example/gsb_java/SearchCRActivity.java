package com.example.gsb_java;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Collections;

public class SearchCRActivity extends AppCompatActivity {
    public static int pointeur = 0;
    public static String nom_praticien;
    public static String prenom_praticien;
    public static JSONArray rapport_visite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcr);

        try {
            ApiRequest.makeRequest(this.getApplicationContext(), this,"praticien","Praticien/read.php", Request.Method.POST, GlobalContext.api, null, "erreurprat");
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
                nom_praticien = GlobalContext.response.getJSONArray("praticiens").getJSONObject(position).getString("nom_praticien");
                prenom_praticien = GlobalContext.response.getJSONArray("praticiens").getJSONObject(position).getString("prenom_praticien");
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

    public void searchRapport(View view){
        try {
            ApiRequest.makeRequest(this.getApplicationContext(), this,"create_rapport","Rapport_visite/read_one.php?id_praticien="+GlobalContext.id_praticien, Request.Method.POST, GlobalContext.api, null, "erreur_create_rapport");
        } catch (ApiRequest.APICallbackException e) {
            e.printStackTrace();
            connectError();
        }

    }
    @APICallback(tag = "create_rapport")
    public void testCallback3(JSONObject response) throws JSONException {
        Log.v("create_rapport", response.toString());
        setContentView(R.layout.activity_displayrapport);
        TextView date = findViewById(R.id.date);
        TextView motif = findViewById(R.id.motif);
        TextView bilan = findViewById(R.id.bilan);
        TextView praticien = findViewById(R.id.praticien);
        JSONArray array_rapport = response.getJSONArray("rapport_visite");
        rapport_visite = array_rapport;
        date.setText("Date: "+array_rapport.getJSONObject(pointeur).getString("date"));
        motif.setText("Motif: "+array_rapport.getJSONObject(pointeur).getString("motif"));
        bilan.setText("Bilan: "+array_rapport.getJSONObject(pointeur).getString("bilan"));
        praticien.setText("Praticien: " + nom_praticien + " " + prenom_praticien);
        Log.v("taille", String.valueOf(array_rapport.length()));
        Button button = findViewById(R.id.suivant);
        Button button1 = findViewById(R.id.precedent);
        if(array_rapport.length() > (pointeur+1)){
            button.setVisibility(View.VISIBLE);
        }else{
            button.setVisibility(View.INVISIBLE);
        }
        if(pointeur == 0){
            button1.setVisibility(View.INVISIBLE);
        }else{
            button1.setVisibility(View.VISIBLE);
        }
    }
    @APICallback(tag= "erreur_create_rapport")
    public void connectError3() {
        //((TextView) findViewById(R.id.textView2)).setText(getString(R.string.errorConnect));
        Log.v("errorConnect", "Erreur");
    }
    public void backMenu(View view) {
        Intent intent = new Intent(SearchCRActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void setSuivant(View view) throws JSONException {
        pointeur++;
        Log.v("pointeur", String.valueOf(pointeur));
        TextView date = findViewById(R.id.date);
        TextView motif = findViewById(R.id.motif);
        TextView bilan = findViewById(R.id.bilan);
        TextView praticien = findViewById(R.id.praticien);
        date.setText("Date: "+rapport_visite.getJSONObject(pointeur).getString("date"));
        motif.setText("Motif: "+rapport_visite.getJSONObject(pointeur).getString("motif"));
        bilan.setText("Bilan: "+rapport_visite.getJSONObject(pointeur).getString("bilan"));
        praticien.setText("Praticien: " + nom_praticien + " " + prenom_praticien);
        Log.v("taille", String.valueOf(rapport_visite.length()));
        Log.v("taille", String.valueOf(pointeur+1));
        Button button = findViewById(R.id.suivant);
        Button button1 = findViewById(R.id.precedent);

        if(rapport_visite.length() > (pointeur+1)){
            button.setVisibility(View.VISIBLE);
        }else{
            button.setVisibility(View.INVISIBLE);
        }
        if(pointeur == 0){
            button1.setVisibility(View.INVISIBLE);
        }else{
            button1.setVisibility(View.VISIBLE);
        }
    }

    public void setPrecedent(View view) throws JSONException {
        pointeur--;
        Log.v("pointeur", String.valueOf(pointeur));
        TextView date = findViewById(R.id.date);
        TextView motif = findViewById(R.id.motif);
        TextView bilan = findViewById(R.id.bilan);
        TextView praticien = findViewById(R.id.praticien);
        date.setText("Date: "+rapport_visite.getJSONObject(pointeur).getString("date"));
        motif.setText("Motif: "+rapport_visite.getJSONObject(pointeur).getString("motif"));
        bilan.setText("Bilan: "+rapport_visite.getJSONObject(pointeur).getString("bilan"));
        praticien.setText("Praticien: " + nom_praticien + " " + prenom_praticien);
        Log.v("taille", String.valueOf(rapport_visite.length()));
        Log.v("taille", String.valueOf(pointeur+1));
        Button button = findViewById(R.id.suivant);
        Button button1 = findViewById(R.id.precedent);

        if(rapport_visite.length() > (pointeur+1)){
            button.setVisibility(View.VISIBLE);
        }else{
            button.setVisibility(View.INVISIBLE);
        }
        if(pointeur == 0){
            button1.setVisibility(View.INVISIBLE);
        }else{
            button1.setVisibility(View.VISIBLE);
        }
    }

}

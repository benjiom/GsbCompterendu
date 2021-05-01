package com.example.gsb_java;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import org.json.JSONException;
import org.json.JSONObject;

public class SigninActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

    }

    public void inscription(View view) throws JSONException {
        // username visiteur
        EditText username = findViewById(R.id.username);
        // password visiteur
        EditText password = findViewById(R.id.password);
        String id = username.getText().toString();
        String mdp = password.getText().toString();
        JSONObject user = new JSONObject();
        user.put("user", id);
        user.put("pwd",mdp);
        GlobalContext.api = user;
        Log.v("connectResponse", user.toString());
        try {
            Log.v("connectAttempt", id);
            ApiRequest.makeRequest(this.getApplicationContext(), this,"connectUser","token.php", Request.Method.POST, user, null, "errorConnect");
        } catch (ApiRequest.APICallbackException e) {
            e.printStackTrace();
            connectError();
        }
    }
    @APICallback(tag = "connectUser")
    public void testCallback(JSONObject response) throws JSONException {
        Log.v("connectResponse", response.toString());
        String token = response.getString("token");
        String valid = response.getString("valid");
        GlobalContext.volid = valid;
        GlobalContext.token = token;
        GlobalContext.api.put("token", token);
        GlobalContext.api.put("valid", valid);

        Log.v("connectResponse", "Cest bon : "+valid);
        Log.v("connectResponse", "Cest bon : "+token);
        Intent intent = new Intent(SigninActivity.this, MenuActivity.class);
        startActivity(intent);
    }
    @APICallback(tag= "errorConnect")
    public void connectError() {
        //((TextView) findViewById(R.id.textView2)).setText(getString(R.string.errorConnect));
        Log.v("errorConnect", "Erreur");
    }
}


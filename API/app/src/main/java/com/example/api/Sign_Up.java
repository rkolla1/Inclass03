package com.example.api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Sign_Up extends AppCompatActivity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPref.edit();

        final EditText username = findViewById(R.id.username_signup);
        final EditText password = findViewById(R.id.password_signup);
        final EditText age = findViewById(R.id.age_signup);
        final EditText weight = findViewById(R.id.weight_signup);
        final EditText address = findViewById(R.id.city_signup);
        final EditText email = findViewById(R.id.email_signup);

        Button register = findViewById(R.id.btn_register);
        Button cancel   = findViewById(R.id.btn_cancel);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://139.59.24.102:5000/create_user?username="+ username.getText().toString() + "&password=" + password.getText().toString() + "&age=" +
                        age.getText().toString()+"&weight=" + weight.getText().toString() + "&address="+ address.getText().toString()+"&email=" + email.getText().toString();

                RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject j = new JSONObject(response);
                            if(j.has("logged_in")) {
                                if (j.getString("logged_in").equals("true")) {
                                    mEdit.putString("token", j.getString("token"));
                                    mEdit.putString("email", email.getText().toString());
                                    mEdit.putString("pass",password.getText().toString());
                                    mEdit.commit();
                                    Toast.makeText(getApplicationContext(), "user account created", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Sign_Up.this, MainActivity.class);
                                    startActivity(i);
                                }
                            }
                            else if(j.has("reason")) {
                                    Toast.makeText(getApplicationContext(), j.getString("reason"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });
                rq.add(sr);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Sign_Up.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}

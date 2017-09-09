package com.hello.tsk.volley;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class MainVolleyActivity extends AppCompatActivity implements Response.Listener,
        Response.ErrorListener {
    public static final String REQUEST_TAG = "MainVolleyActivity";
    private TextView cTextView;
    private TextView nTextView;
    private Button cButton;
    private Button nButton;
    private EditText cEditText;
    private EditText nEditText;
    private RequestQueue mQueue;
    //Context context = getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_volley);

        cTextView = (TextView) findViewById(R.id.cTextView);
        cButton = (Button) findViewById(R.id.cButton);
        nTextView = (TextView) findViewById(R.id.nTextView);
        nButton = (Button) findViewById(R.id.nButton);
        cEditText = (EditText) findViewById(R.id.barcode);
        nEditText = (EditText) findViewById(R.id.name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Instantiate the RequestQueue.


        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = cEditText.getText().toString();
                String url = "http://192.168.0.1:8080/search/code/" + code;

                JsonArrayRequest jsonRequest = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // the response is already constructed as a JSONObject!
                                try {
                                    cTextView.setText("Response = " + response);
                                    JSONObject res = response.getJSONObject(0);
                                    String unitPrice = res.getString("Unit Price");
                                    //"Product Name" to get product name
                                    //"Barcode" for barcode no
                                    //"Category" to know the category

                                    cTextView.setText(cTextView.getText() + "\nUnit Price = " + unitPrice);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    cTextView.setText(e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                cTextView.setText(error.getMessage());
                            }
                        });

                Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);

            }
        });

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nEditText.getText().toString();
                String url = "http://192.168.0.1:8080/search/name/" + name;

                JsonArrayRequest jsonRequest = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // the response is already constructed as a JSONObject!
                                try {
                                    nTextView.setText("Response = " + response);
                                    JSONObject res = response.getJSONObject(0);

                                    String unitPrice = res.getString("Unit Price");
                                    //"Unit Price" for unitPrice
                                    //"Rack No" to get the rack no
                                    //"Shelf No" to get shelf no
                                    //"Product Name" to get the name of product
                                    Log.i("Unit Price : ",unitPrice);

                                    nTextView.setText(nTextView.getText() + "\nUnit Price = " + unitPrice);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    nTextView.setText(e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                nTextView.setText(error.getMessage());
                            }
                        });

                Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        cTextView.setText(error.getMessage());
    }

    @Override
    public void onResponse(Object response) {
        cTextView.setText("Response is: " + response);
        try {
            cTextView.setText(cTextView.getText() + "\n\n" + ((JSONObject) response).getString
                    ("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
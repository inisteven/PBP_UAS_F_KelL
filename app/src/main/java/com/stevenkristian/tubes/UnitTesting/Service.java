package com.stevenkristian.tubes.UnitTesting;


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.model.Motor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class Service {
    public void login(final CreateView createView, String merk, String warna, String plat,
                      String tahun, String status, String harga, Context context,
                      final Callback callback){
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest stringRequest = new JsonObjectRequest(POST, MotorAPI.URL_ADD,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray object = response.getJSONArray("data");

                    if(response.optString("message").equals("Add Motor Success"))
                    {
                        JSONObject object1 = (JSONObject) object.get(0);
                        int id               = object1.optInt("id");
                        String merk          = object1.optString("merk");
                        String warna         = object1.optString("warna");
                        String plat          = object1.optString("plat");
                        String tahun         = object1.optString("tahun");
                        String status        = object1.optString("status");
                        double harga         = object1.optDouble("harga");
                        String imgURL        = object1.optString("imgURL");

                        //Membuat objek user
                        Motor motor = new Motor(id, merk, warna, plat, tahun, status, harga, imgURL);
                        callback.onSuccess(true, motor);
                    }else{
                        callback.onError();
                        createView.showCreateError(response.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                createView.showErrorResponse(error.getMessage());
                callback.onError();
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("merk",merk);
                params.put("warna",warna);
                params.put("plat",plat);
                params.put("tahun",tahun);
                params.put("status",status);
                params.put("harga", harga);
                params.put("imgURL","-");

                return params;
            }
        };
        queue.add(stringRequest);
    }

    public Boolean getValid(final CreateView view, String merk, String warna, String plat,
                            String tahun, String status, String harga,Context context){
        final Boolean[] bool = new Boolean[1];
        login(view, merk, warna, plat, tahun, status, harga, context, new Callback() {
            @Override
            public void onSuccess(boolean value, Motor motor) {
                bool[0] = true;
            }
            @Override
            public void onError() {
                bool[0] = false;
            }
        });
        return bool[0];
    }
}

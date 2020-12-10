package com.stevenkristian.tubes.admin;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stevenkristian.tubes.Histori;
import com.stevenkristian.tubes.R;
import com.stevenkristian.tubes.admin.adapter.MotorAdapterAdmin;
import com.stevenkristian.tubes.api.HistoryAPI;
import com.stevenkristian.tubes.model.History;
import com.stevenkristian.tubes.model.Motor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class viewHistoryAdmin extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapterAdmin adapter;
    private List<History> historyList;
    private View view;
    private FloatingActionButton floatingButtonPlus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_history_admin, container, false);

        floatingButtonPlus = (FloatingActionButton) view.findViewById(R.id.floatingButtonPlus);

        floatingButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putString("status", "tambah");
                TambahEditMotorAdmin tambahEditMotor = new TambahEditMotorAdmin();
                tambahEditMotor.setArguments(data);
                floatingButtonPlus.hide();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager .beginTransaction()
                        .replace(R.id.frame_view_motor_admin, tambahEditMotor)
                        .commit();


            }
        });

        loadDaftarHistory();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void loadDaftarHistory(){
        setAdapter();
        getHistory();
    }

    public void setAdapter(){
        getActivity().setTitle("Data Motor");
        /*Buat tampilan untuk adapter jika potrait menampilkan 2 data dalam 1 baris,
        sedangakan untuk landscape 4 data dalam 1 baris*/

        //biasa
        historyList = new ArrayList<History>();
        getHistory();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new HistoryAdapterAdmin(view.getContext(), historyList, new HistoryAdapterAdmin.deleteItemListener() {
            @Override
            public void deleteItem(Boolean delete) {
                if(delete){
                    loadDaftarHistory();
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getHistory() {
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data history transaksi");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, HistoryAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data motor
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!historyList.isEmpty())
                        historyList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id               = jsonObject.optInt("id");
                        String id_user             = jsonObject.optString("id_user");
                        String id_motor            = jsonObject.optString("id_motor");
                        String tglPinjam                = jsonObject.optString("tglPinjam");
                        String tglKembali               = jsonObject.optString("tglKembali");

                        //Membuat objek user
                        History history = new History(id,id_user,id_motor,tglPinjam,tglKembali);
                        //Menambahkan objek user tadi ke list user
                        historyList.add(history);
                        //Toast.makeText(Histori.this, historyList.get(0).getId(), Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}
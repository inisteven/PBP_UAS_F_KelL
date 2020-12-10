package com.stevenkristian.tubes.admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.stevenkristian.tubes.PdfViewModel;
import com.stevenkristian.tubes.R;
import com.stevenkristian.tubes.admin.adapter.MotorAdapterAdmin;
import com.stevenkristian.tubes.admin.adapter.UserAdapterAdmin;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.api.UserAPI;
import com.stevenkristian.tubes.model.Motor;
import com.stevenkristian.tubes.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class viewsUserAdmin extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapterAdmin adapter;
    private List<User> ListUser;
    private View view;
    private FloatingActionButton floatingButtonPlus;

    //cetak
    private PdfViewModel pdfViewModel;
    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 101;
    private File pdfFile;
    private PdfWriter writer;
    private AlertDialog.Builder builder;
    private Button btnCetak;
    private ImageButton btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_views_user_admin, container, false);

        floatingButtonPlus = (FloatingActionButton) view.findViewById(R.id.floatingButtonPlus);
        btnCetak = view.findViewById(R.id.btnCetak);

        btnCetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());

                builder.setCancelable(false);
                builder.setMessage("Apakah anda yakin ingin mencetak surat pemesanan ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            createPdfWrapper();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.WHITE);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.WHITE);
            }
        });
        floatingButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putString("status", "tambah");
                TambahEditUserAdmin tambahEditUser = new TambahEditUserAdmin();
                tambahEditUser.setArguments(data);
                floatingButtonPlus.hide();
                btnCetak.setVisibility(View.GONE);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager .beginTransaction()
                        .replace(R.id.frame_view_user_admin, tambahEditUser)
                        .commit();


            }
        });

        loadDaftarUser();

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });



        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void loadDaftarUser(){
        setAdapter();
        getUser();
    }



    public void setAdapter(){
        getActivity().setTitle("Data User");
        /*Buat tampilan untuk adapter jika potrait menampilkan 2 data dalam 1 baris,
        sedangakan untuk landscape 4 data dalam 1 baris*/

        //biasa
        ListUser = new ArrayList<User>();
        getUser();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new UserAdapterAdmin(view.getContext(), ListUser, new MotorAdapterAdmin.deleteItemListener() {
            @Override
            public void deleteItem(Boolean delete) {
                if(delete){
                    loadDaftarUser();
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getUser() {
        //Tambahkan tampil motor disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data user");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, UserAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data motor
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!ListUser.isEmpty())
                        ListUser.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id               = jsonObject.optInt("id");
                        String name             = jsonObject.optString("name");
                        String email            = jsonObject.optString("email");
                        String password                = jsonObject.optString("password");
                        String phone               = jsonObject.optString("phone");
                        String ktp               = jsonObject.optString("ktp");
                        String imgURL               = jsonObject.optString("imgURL");

                        //Membuat objek user
                        User user = new User(id,name,email, password, phone,ktp,imgURL);

                        //Menambahkan objek user tadi ke list user
                        ListUser.add(user);
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

    private void createPdf() throws FileNotFoundException, DocumentException {
        //isikan code createPdf()
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Download/");
        if (!docsFolder.exists())
        { docsFolder.mkdir();
            Log.i(TAG, "Direktori baru untuk file pdf berhasil dibuat");
        }

        String pdfname = "Laporan Pengguna Aplikasi Motorant"+".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        writer = PdfWriter.getInstance(document, output);
        document.open();

        Paragraph judul = new Paragraph("Laporan Pengguna Aplikasi Motorant \n\n", new
                Font(Font.FontFamily.TIMES_ROMAN, 16,
                Font.BOLD, BaseColor.BLACK));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);
        PdfPTable tables = new PdfPTable(new float[]{16, 8});
        tables.getDefaultCell().setFixedHeight(50);
        tables.setTotalWidth(PageSize.A4.getWidth());
        tables.setWidthPercentage(100);
        tables.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cellSupplier = new PdfPCell();
        cellSupplier.setPaddingLeft(20);
        cellSupplier.setPaddingBottom(10);
        cellSupplier.setBorder(Rectangle.NO_BORDER);



        Font f = new
                Font(Font.FontFamily.TIMES_ROMAN, 10,
                Font.NORMAL, BaseColor.BLACK);
        Paragraph Pembuka = new Paragraph("\nBerikut merupakan para pengguna aplikasi motorant: \n\n",f);
        Pembuka.setIndentationLeft(20); document.add(Pembuka);
        PdfPTable tableHeader = new PdfPTable(new float[]{5,5,5,5});
        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.getDefaultCell().setFixedHeight(30);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);
        //TODO 2.5 - Bagian ini tidak perlu diubah
        PdfPCell h1 = new PdfPCell(new Phrase("Nama Lengkap"));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);

        PdfPCell h2 = new PdfPCell(new Phrase("Email"));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);

        PdfPCell h3 = new PdfPCell(new Phrase("Nomor Telefon"));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setPaddingBottom(5);

        PdfPCell h4 = new PdfPCell(new Phrase("No. KTP"));
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);
        h4.setPaddingBottom(5);

        tableHeader.addCell(h1);
        tableHeader.addCell(h2);
        tableHeader.addCell(h3);
        tableHeader.addCell(h4);
        PdfPCell[] cells = tableHeader.getRow(0).getCells();
        for(int j=0;j<cells.length;j++){
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
        document.add(tableHeader);
        PdfPTable tableData = new PdfPTable(new float[]{5,5,5,5});
        tableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableData.getDefaultCell().setFixedHeight(30);
        tableData.setTotalWidth(PageSize.A4.getWidth());
        tableData.setWidthPercentage(100);
        tableData.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        int arrLength = ListUser.size();

        for(int x=0;x<arrLength;x++){
            for(int i=0;i<cells.length;i++){
                if(i==0){ tableData.addCell(ListUser.get(x).getFullname());
                }else if(i==1){
                    tableData.addCell(ListUser.get(x).getEmail());
                }else if(i==2){
                    tableData.addCell(ListUser.get(x).getPhone());
                }else{
                    tableData.addCell((ListUser.get(x).getKtp()));
                }

            }
        }
        document.add(tableData);
        Font h = new
                Font(Font.FontFamily.TIMES_ROMAN, 10,
                Font.NORMAL);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String tglDicetak = sdf.format(currentTime);
        Paragraph P = new Paragraph("\nDicetak tanggal " + tglDicetak, h);
        P.setAlignment(Element.ALIGN_RIGHT);
        document.add(P);
        document.close();
        previewPdf();

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

        //isikan code createPdfWrapper()
        int hasWriteStoragePermission = 0;
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("Izinkan aplikasi untuk akses penyimpanan?",
                            new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }

    }

    private void previewPdf() {

        //isikan code previewPdf()
        PackageManager packageManager = getContext().getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(getActivity(), getContext().getPackageName()+".provider", pdfFile);
            }else {
                uri = Uri.fromFile(pdfFile);
            }
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(uri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            //TODO 2.6 - Sesuaikan package dengan package yang anda buat
            getContext().grantUriPermission("com.stevenkristian.tubes.admin", uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(pdfIntent);
        } else {
            Toast.makeText(getActivity(), "Unduh Pembuka PDF untuk menampilkan file ini", Toast.LENGTH_SHORT).show();

        }
    }

}
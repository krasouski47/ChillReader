package com.evg47.myapplication_03_11.app.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.evg47.myapplication_03_11.R;
import com.evg47.myapplication_03_11.ViewPDFFiles;
import com.evg47.myapplication_03_11.app.adapters.PDFAdapter;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv_pdf;
    public static ArrayList<File> fileList = new ArrayList<>();
    static PDFAdapter obj_adapter;
    public static int REQUEST_PERMISSION = 1;
    boolean boolean_permission;
    File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_pdf = (ListView) findViewById(R.id.listView_pdf);

        dir = new File(Environment.getExternalStorageDirectory().toString());
        permission_fn();

        lv_pdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewPDFFiles.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final Toast toast1 = Toast.makeText(this, "Настройки", Toast.LENGTH_LONG);
        final Toast toast2 = Toast.makeText(this, "открыть", Toast.LENGTH_LONG);
        switch (id) {
            case R.id.action_settings:

                toast1.show();
                return true;
            case R.id.open_settings:
                toast2.show();
                return true;
            case R.id.save_settings:
                permission_fn();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void permission_fn() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }
        } else {
            boolean_permission = true;
            getFile(dir);
            obj_adapter = new PDFAdapter(getApplicationContext(), fileList);
            lv_pdf.setAdapter(obj_adapter);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;
                getFile(dir);
                obj_adapter = new PDFAdapter(getApplicationContext(), fileList);
                lv_pdf.setAdapter(obj_adapter);

            } else {
                Toast.makeText(this, "Please Allow the Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<File> getFile(File dir) {

        File listFile[] = dir.listFiles();

        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {

                    getFile(listFile[i]);
                } else {

                    boolean booleanpdf = false;
                    if (listFile[i].getName().endsWith(".pdf")) {

                        for (int j = 0; j < fileList.size(); j++) {

                            if (fileList.get(j).getName().equals(listFile[i].getName())) {
                                booleanpdf = true;
                            } else {

                            }

                        }

                        if (booleanpdf) {
                            booleanpdf = true;
                        } else {
                            Log.i("listFile " + i, "" + listFile[i]);
                            fileList.add(listFile[i]);
                        }
                    }
                }
            }
        }
        return fileList;
    }


}
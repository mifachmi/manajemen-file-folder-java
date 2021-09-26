package com.example.filemanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.filemanagement.databinding.ActivityFileManagementBinding;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class FileManagementActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityFileManagementBinding binding;
    public static String FILENAME = "fileOtomatis";
    public String nameFileAfterRename = "";
    public static final int REQUEST_CODE_STORAGE = 100;
    public int selectEvent = 0;
    public boolean successCreated, successDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFileManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setTitle("File Management");

        binding.buttonBuatFile.setOnClickListener(this);
        binding.buttonBacaFile.setOnClickListener(this);
        binding.buttonHapusFile.setOnClickListener(this);
        binding.buttonUbahFile.setOnClickListener(this);
    }

    public boolean periksaIzinPenyimpanan() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doThis(selectEvent);
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonBuatFile:
            case R.id.buttonBacaFile:
            case R.id.buttonUbahFile:
            case R.id.buttonHapusFile:
                if (periksaIzinPenyimpanan()) {
                    selectEvent = view.getId();
                    doThis(selectEvent);
                }
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void doThis(int id) {
        switch (id) {
            case R.id.buttonBuatFile:
                buatFile();
                break;
            case R.id.buttonBacaFile:
                bacaFile();
                break;
            case R.id.buttonUbahFile:
                ubahFile();
                break;
            case R.id.buttonHapusFile:
                hapusFile();
                break;
        }
    }

    void buatFile() {
        FileOutputStream outputStream;
        String isiFile = "ini isi file tersebut";
        String state = Environment.getExternalStorageState();
        Log.d("msgBuat", state);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILENAME);

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }

        try {
            successCreated = file.createNewFile();
            if (successCreated) {
                Toast.makeText(this, "File " + FILENAME + " Berhasil Dibuat",
                        Toast.LENGTH_SHORT).show();
            }
            outputStream = new FileOutputStream(file, true);
            outputStream.write(isiFile.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void ubahFile() {
        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILENAME);
        binding.textBaca.setText(FILENAME);

        try {
            if(!file.exists()) {
                binding.textBaca.setText(R.string.hasil_baca_file);
                Toast.makeText(this, "Tidak ada file yang perlu diedit", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.btnSave.setOnClickListener(view -> {
                File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), String.valueOf(binding.textBaca.getText()));
                nameFileAfterRename = newFile.toString();
                file.renameTo(newFile);
                Toast.makeText(this, "Berhasil Mengedit Nama File " + FILENAME, Toast.LENGTH_SHORT).show();
                binding.textBaca.setText(R.string.hasil_baca_file);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bacaFile() {
        String fileName = nameFileAfterRename.substring(nameFileAfterRename.lastIndexOf("/")+1);
        File file;

        if (nameFileAfterRename.equals("")) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILENAME);
        } else {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        }

        if(file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();

                while (line != null) {
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Error " + e.getMessage());
            }
            binding.textBaca.setText(text.toString());
        } else {
            Toast.makeText(this, "Tidak ada file ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    void hapusFile() {
        String fileName = nameFileAfterRename.substring(nameFileAfterRename.lastIndexOf("/")+1);
        File file;
        if (nameFileAfterRename.equals("")) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILENAME);

        } else {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

        }
        if (file.exists()) {
            successDeleted = file.delete();
            if (successDeleted) {
                binding.textBaca.setText(R.string.hasil_baca_file);
                Toast.makeText(this, "File " + file.getName() +" Berhasil Dihapus",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Tidak ada file yang harus dihapus",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
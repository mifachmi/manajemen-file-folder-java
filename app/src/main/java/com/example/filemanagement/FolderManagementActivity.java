package com.example.filemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.filemanagement.databinding.ActivityFolderManagementBinding;
import java.io.File;
import java.util.Objects;

public class FolderManagementActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityFolderManagementBinding binding;
    public static String FOLDERNAME = "folderOtomatis";
    public static final int REQUEST_CODE_STORAGE = 100;
    private String nameFolderAfterRename = "";
    public int selectEvent = 0;
    public boolean successCreated, successDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFolderManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Folder Management");

        binding.buttonBuatFolder.setOnClickListener(this);
        binding.buttonUbahFolder.setOnClickListener(this);
        binding.buttonHapusFolder.setOnClickListener(this);
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
            case R.id.buttonBuatFolder:
            case R.id.buttonUbahFolder:
            case R.id.buttonHapusFolder:
                if (periksaIzinPenyimpanan()) {
                    selectEvent = view.getId();
                    doThis(view.getId());
                }
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void doThis(int id) {
        switch (id) {
            case R.id.buttonBuatFolder:
                buatFolder();
                break;
            case R.id.buttonUbahFolder:
                ubahFolder();
                break;
            case R.id.buttonHapusFolder:
                hapusFolder();
                break;
        }
    }

    void buatFolder() {
        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FOLDERNAME);
        try {
            successCreated = folder.mkdirs();
            if (successCreated) {
                Toast.makeText(this, "Folder " + FOLDERNAME + " Berhasil Dibuat",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void ubahFolder() {
        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FOLDERNAME);
        binding.textBaca.setText(FOLDERNAME);

        try {
            if(!folder.exists()) {
                binding.textBaca.setText(R.string.nama_folder);
                Toast.makeText(this, "Tidak ada folder yang perlu direname", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.btnSave.setOnClickListener(view -> {
                File newFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), binding.textBaca.getText().toString());
                nameFolderAfterRename = newFolder.toString();
                folder.renameTo(newFolder);
                Log.d("msgRen", FOLDERNAME);
                Log.d("msgRen", newFolder.toString());
                Toast.makeText(this, "Berhasil Mengedit Nama Folder " + FOLDERNAME, Toast.LENGTH_SHORT).show();
                binding.textBaca.setText(R.string.nama_folder);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void hapusFolder() {
        String folderName = nameFolderAfterRename.substring(nameFolderAfterRename.lastIndexOf("/")+1);
        File folder;

        if (nameFolderAfterRename.equals("")) {
            folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FOLDERNAME);
        } else {
            folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folderName);
        }

        if (folder.exists()) {
            successDeleted = folder.delete();
            if (successDeleted) {
                binding.textBaca.setText(R.string.nama_folder);
                Toast.makeText(this, "Folder " + folder.getName() +" Berhasil Dihapus",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("msg", FOLDERNAME);
            Toast.makeText(this, "Tidak ada folder yang harus dihapus",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
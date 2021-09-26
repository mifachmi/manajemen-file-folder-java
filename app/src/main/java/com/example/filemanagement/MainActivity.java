package com.example.filemanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.filemanagement.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.filemanagement.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnFileManajemen.setOnClickListener(this);
        binding.btnFolderManajemen.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnFileManajemen:
                Intent intentFile = new Intent(MainActivity.this, FileManagementActivity.class);
                startActivity(intentFile);
                break;
            case R.id.btnFolderManajemen:
                Intent intentFolder = new Intent(MainActivity.this, FolderManagementActivity.class);
                startActivity(intentFolder);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileManagementActivity file = new FileManagementActivity();
        file.hapusFile();
    }
}
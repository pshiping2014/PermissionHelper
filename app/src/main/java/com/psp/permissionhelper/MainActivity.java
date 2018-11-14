package com.psp.permissionhelper;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
/**
 * Created by psp on 2018/11/12.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, PermissionHelper.PermissionListener {

   private  Button btn_request_storage;
   private  Button btn_request_camera;
   private LinearLayout ll_fg_container;

   public static final int PERMISSIONS_REQUEST_CODE_STORAGE = 100;
   public static final int PERMISSIONS_REQUEST_CODE_CAMERA = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_request_storage = findViewById(R.id.btn_request_storage);
        btn_request_camera = findViewById(R.id.btn_request_camera);
        ll_fg_container = findViewById(R.id.ll_fg_container);
        btn_request_storage.setOnClickListener(this);
        btn_request_camera.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_request_storage:
                PermissionHelper.create(this)
                        .addListener(this)
                        .addPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .addRequestCode(PERMISSIONS_REQUEST_CODE_STORAGE)
                        .requestPermissions();
                break;

            case R.id.btn_request_camera:
                PermissionHelper.create(this)
                        .addListener(this)
                        .addPermissions(Manifest.permission.CAMERA)
                        .addRequestCode(PERMISSIONS_REQUEST_CODE_CAMERA)
                        .requestPermissions();
                break;
                default:
                    break;

        }
    }

    @Override
    public void doRequestSuccess(Object object, int requestCode) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_CODE_STORAGE:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ll_fg_container, ImageFragment.newInstance())
                        .commitAllowingStateLoss();
                break;
            case PERMISSIONS_REQUEST_CODE_CAMERA:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
                break;

        }

    }

    @Override
    public void doRequestFail(Object object, int requestCode) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_CODE_STORAGE:
                Toast.makeText(this, "Storage permission is not granted", Toast.LENGTH_SHORT).show();
                break;
            case PERMISSIONS_REQUEST_CODE_CAMERA:
                Toast.makeText(this, "Camera permission is not granted", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionHelper.create(this).onRequestPermissionsResult(this,requestCode,permissions,grantResults);
        Log.d("psp","act onRequestPermissionsResult");
    }
}

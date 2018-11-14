package com.psp.permissionhelper;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import static com.psp.permissionhelper.MainActivity.PERMISSIONS_REQUEST_CODE_STORAGE;
/**
 * Created by psp on 2018/11/12.
 */
public class ImageFragment extends Fragment implements PermissionHelper.PermissionListener {

    public ImageFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ImageFragment.
     */
    public static ImageFragment newInstance() {
        ImageFragment fragment = new ImageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ImageView iv_display_img  = view.findViewById(R.id.iv_display_image);
        PermissionHelper.create(ImageFragment.this)
                .addListener(this)
                .addPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .addRequestCode(PERMISSIONS_REQUEST_CODE_STORAGE)
                .requestPermissions();

        Bitmap bitmap = BitmapFactory.
                decodeFile(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/img1.jpg");
        iv_display_img.setImageBitmap(bitmap);

        return view;

    }


    @Override
    public void doRequestSuccess(Object object, int requestCode) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_STORAGE) {
            Toast.makeText(getActivity(), "Storage permission is granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void doRequestFail(Object object, int requestCode) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_STORAGE) {
            Toast.makeText(getActivity(), "Storage permission is granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionHelper.create(this).onRequestPermissionsResult(this,requestCode,permissions,grantResults);
        Log.d("psp","fg onRequestPermissionsResult");
    }
}

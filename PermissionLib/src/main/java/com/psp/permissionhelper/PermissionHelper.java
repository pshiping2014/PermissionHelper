package com.psp.permissionhelper;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psp on 2018/11/12.
 */

public class PermissionHelper {

    private String[] mPermissions;
    private int mRequestCode;
    private Object object;
    private PermissionListener mRequestListener;

    private static PermissionHelper mPermissionHelper;

    private PermissionHelper(Object obj) {
        this.object = obj;
    }

    public static PermissionHelper create(Object obj) {
        if (mPermissionHelper != null) {
            return mPermissionHelper;
        } else {
            mPermissionHelper = new PermissionHelper(obj);
            return mPermissionHelper;
        }
    }

    public PermissionHelper addListener(PermissionListener listener) {
        this.mRequestListener = listener;
        return this;
    }

    public PermissionHelper addPermissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    public PermissionHelper addRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions() {
        requestPermissions(object, mPermissions, mRequestCode);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions(Object object, String[] permissions, int requestCode) {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            doRequestSuccess(object, requestCode);
            return;
        }

        List<String> rejectedPermissions = findRejectedPermissions(getActivity(object), permissions);

        if (rejectedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(rejectedPermissions.toArray(new String[rejectedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(rejectedPermissions.toArray(new String[rejectedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
            }

        } else {
            doRequestSuccess(object, requestCode);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static List<String> findRejectedPermissions(Activity activity, String... permission) {
        List<String> rejectedPermissions = new ArrayList<>();
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                rejectedPermissions.add(value);
            }
        }
        return rejectedPermissions;
    }

    public static Activity getActivity(Object object) {
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            return (Activity) object;
        }
        return null;
    }


    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,
                                           int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    public void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions,
                                           int[] grantResults) {
        requestResult(fragment, requestCode, permissions, grantResults);
    }

    private void requestResult(Object obj, int requestCode, String[] permissions,
                               int[] grantResults) {
        List<String> rejectedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                rejectedPermissions.add(permissions[i]);
            }
        }

        if (rejectedPermissions.size() > 0) {
            doRequestFail(obj, requestCode);
        } else {
            doRequestSuccess(obj, requestCode);
        }
    }

    public void doRequestSuccess(Object object, int requestCode) {
//        Log.w("psp", new Throwable("doRequestSuccess"));
        if (mRequestListener != null) {
            mRequestListener.doRequestSuccess(object, requestCode);
        }

    }

    private void doRequestFail(Object object, int requestCode) {
//        Log.w("psp", new Throwable("doRequestFail"));
        if (mRequestListener != null) {
            mRequestListener.doRequestFail(object, requestCode);
        }
    }

    public interface PermissionListener {
        void doRequestSuccess(Object object, int requestCode);

        void doRequestFail(Object object, int requestCode);

    }

}

package com.huong.mycinema.util;

import android.app.Activity;

import com.huong.mycinema.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by HuongPN on 10/24/2018.
 */
public class DialogUtils {

    public static SweetAlertDialog showProgress(Activity activity){
        final SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.colorPrimaryDark);
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        return pDialog;
    }

    public static void showDialogError(Activity activity, String error){
        final SweetAlertDialog pDialog = new SweetAlertDialog(activity);
        pDialog.setTitleText("Thông báo lỗi");
        pDialog.setContentText(error);
        pDialog.setConfirmText("Ok");
        pDialog.setCancelable(false);
        pDialog.show();
    }
}

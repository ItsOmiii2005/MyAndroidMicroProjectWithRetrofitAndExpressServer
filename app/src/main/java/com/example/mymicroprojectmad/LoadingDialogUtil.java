package com.example.mymicroprojectmad;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

public class LoadingDialogUtil {

    private final Dialog loadingDialog;

    public LoadingDialogUtil(Context context) {
        loadingDialog = new Dialog(context);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setCancelable(false);

        loadingDialog.setContentView(R.layout.dialog_loading);
    }

    public void showLoadingDialog(String message) {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            TextView loadingText = loadingDialog.findViewById(R.id.loading_text);
            loadingText.setText(message);
            loadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}

package biz.sendyou.senduandroid;

import android.app.ProgressDialog;

/**
 * Created by parkjaesung on 2016. 10. 6..
 */
/*
Usage
ProgressDialog = ProgressDialogManager.getInstance().showNewDialog(...);

...
ProgressDialog.dismiss();
 */
public class ProgressDialogManager {

    private static ProgressDialogManager instance = new ProgressDialogManager();

    private ProgressDialogManager(){

    }

    public static ProgressDialogManager getInstance(){
        return instance;
    }

    //public methods
    public ProgressDialog showNewDialog(String title, String text){
        return ProgressDialog.show(ContextManager.getContext(), title, text, true, false);
    }
}

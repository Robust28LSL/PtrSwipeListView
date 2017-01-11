package com.it.juyou;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by lishuliang on 2016/12/8.
 */

public class BaseHandler<T>  extends Handler {
    private WeakReference<T> actref;

    public BaseHandler(T act) {
        actref = new WeakReference<T>(act);
    }

    @Override
    public void handleMessage(Message msg) {
        T act = actref.get();

        if (act == null) {
            return;
        }



        if (msg.what == 1) {


        } else {

        }

        super.handleMessage(msg);
    }



}

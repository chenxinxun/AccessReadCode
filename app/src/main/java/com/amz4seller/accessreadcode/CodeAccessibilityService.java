package com.amz4seller.accessreadcode;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cxx on 2017/12/15.
 */

public class CodeAccessibilityService extends AccessibilityService {
    CodeService service;
    LinkedList<CodeInfo> history =  new LinkedList<>();
    boolean isDown = true;
    boolean dataChange = false;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try{
            AccessibilityNodeInfo info = event.getSource();
            boolean pushData = false;
            for (int i = 0; i < info.getChildCount(); i++) {
                AccessibilityNodeInfo subInfo = info.getChild(i);
                CodeInfo code = new CodeInfo();
                for (int j = 0; j < subInfo.getChildCount(); j++){
                     AccessibilityNodeInfo detailInfo = subInfo.getChild(j);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        detailInfo.refresh();
                    }
                    if (detailInfo.getText() == null) {
                        continue;
                    }
                    String text  = detailInfo.getText().toString();
                    if (j == 0) {
                        code.code = text;
                    } else {
                        code.account = text;
                    }
                    detailInfo.recycle();
                }
                if (history.contains(code)) {
                    int idx = history.indexOf(code);
                    CodeInfo last = history.get(idx);
                    if (!TextUtils.equals(last.code, code.code)) {
                        pushData = true;
                        history.set(idx, code);
                    }
                } else {
                    if (code.account == null || code.code == null) {

                    } else {
                        pushData=true;
                        history.add(code);
                    }

                }
                subInfo.recycle();

            }

            if (service == null){
                service = RetrofitService.getInstance().createApi(CodeService.class);
            }
            if (pushData) {
                Log.d("Code change", history.toString());
                Call<BaseBean> result = service.pushCodeInfos(history);
                dataChange =  true;
                result.enqueue(new Callback<BaseBean>() {
                    @Override
                    public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                        try{ Log.d("response", response.body().content.toString());}catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<BaseBean> call, Throwable t) {
                        try{Log.e("error", t.getMessage());}catch (Exception e) {

                        }
                    }

                });
            } else {
                if (isDown) {
                    if (dataChange){
                        if (info.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {

                        } else {
                            isDown = !isDown;
                            dataChange = false;
                            return;
                        }
                    }
                } else  {
                    if (dataChange) {
                        if (info.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)){
                        } else {
                            isDown = !isDown;
                            dataChange = false;
                            return;
                        }
                    }
                }

            }



            info.recycle();
        }catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {

    }
}

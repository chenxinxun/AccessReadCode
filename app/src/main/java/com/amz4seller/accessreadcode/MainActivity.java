package com.amz4seller.accessreadcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private ChangeNetType changeNetType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyout_main);
        RadioGroup gruop = findViewById(R.id.net_type);
        changeNetType = RetrofitService.getInstance();
        gruop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.debug:
                        UrlConst.REQUEST_URL = UrlConst.DEBUG_URL;
                        changeNetType.notifyChange();
                        break;
                    case R.id.release:
                        UrlConst.REQUEST_URL = UrlConst.RELEASE_URL;
                        changeNetType.notifyChange();
                        break;
                }
            }
        });
    }
}

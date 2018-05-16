package com.amz4seller.accessreadcode;

import android.text.TextUtils;

/**
 * Created by cxx on 2017/12/19.
 */

public class CodeInfo {
    public String code;
    public String account;

    @Override
    public boolean equals(Object obj) {
        return TextUtils.equals(account, ((CodeInfo)obj).account);

    }


    @Override
    public String toString() {
        return "code:" + code + ", account: "+account;
    }
}

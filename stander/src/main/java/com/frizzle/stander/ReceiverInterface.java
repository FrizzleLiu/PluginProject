package com.frizzle.stander;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * author: LWJ
 * date: 2020/9/8$
 * description
 */
public interface ReceiverInterface {
    void onReceive(Context context, Intent intent);
}

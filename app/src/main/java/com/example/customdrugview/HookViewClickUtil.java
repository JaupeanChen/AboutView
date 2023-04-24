package com.example.customdrugview;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Author: ChenGuiPing
 * Date: 2023/4/18
 * Description: 防抖点击hook类
 */
public class HookViewClickUtil {

    public static HookViewClickUtil getInstance() {
        return UtilHolder.mHookViewClickUtil;
    }

    private static class UtilHolder {
        private static HookViewClickUtil mHookViewClickUtil = new HookViewClickUtil();
    }

    public static void hookView(View view) {
        try {
            Class<?> viewClazz = Class.forName("android.view.View");
            @SuppressLint("DiscouragedPrivateApi")
            Method listenerInfoMethod = viewClazz.getDeclaredMethod("getListenerInfo");
            if (!listenerInfoMethod.isAccessible()) {
                listenerInfoMethod.setAccessible(true);
            }
            Object listenerInfoObj = listenerInfoMethod.invoke(view); //获取ListenerInfo实例

            @SuppressLint("PrivateApi")
            Class<?> listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
            @SuppressLint("DiscouragedPrivateApi")
            Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");
            if (!onClickListenerField.isAccessible()) {
                onClickListenerField.setAccessible(true);
            }
            //获取mOnClickListener实例, 这个时候当前实例view的mOnClickListener也就拿到了
            View.OnClickListener mOnClickListener = (View.OnClickListener) onClickListenerField.get(listenerInfoObj);

            //自定义代理事件监听器
            View.OnClickListener onClickListenerProxy = new OnClickListenerProxy(mOnClickListener);
            //更换
            onClickListenerField.set(listenerInfoObj, onClickListenerProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //自定义的代理事件监听器
    private static class OnClickListenerProxy implements View.OnClickListener {

        private View.OnClickListener object;

        private int MIN_CLICK_DELAY_TIME = 1000;

        private long lastClickTime = 0;

        private OnClickListenerProxy(View.OnClickListener object) {
            this.object = object;
        }

        @Override
        public void onClick(View v) {
            Log.d("ClickProxy", "Hook View Point");
            //点击时间控制
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
//                Log.e("OnClickListenerProxy", "OnClickListenerProxy");
                Log.d("ClickProxy", "防抖时间正常，传递点击事件");
                if (object != null) object.onClick(v);
            }
        }
    }
}

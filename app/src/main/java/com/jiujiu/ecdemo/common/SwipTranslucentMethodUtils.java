package com.jiujiu.ecdemo.common;

import android.app.Activity;
import android.app.ActivityOptions;
import android.util.Log;

import com.jiujiu.ecdemo.common.utils.LogUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SwipTranslucentMethodUtils {

    private static final String TAG = "ECSDK_Demo.SwipTranslucentMethodUtils";
    private SwipTranslucentMethodUtils() {
    }

    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable t) {
        }
    }

    public static void convertActivityToTranslucent(Activity activity , MethodInvoke.SwipeInvocationHandler handler) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }

            //if(translucentConversionListenerClazz != null) {
            Object proxy = Proxy.newProxyInstance(translucentConversionListenerClazz.getClassLoader(), new Class[] {translucentConversionListenerClazz}, handler);
            //}
            LogUtil.d(TAG , "proxy " + proxy);
            if(!SDKVersionUtils.isGreaterorEqual(21)) {
                Method method = Activity.class.getDeclaredMethod("convertToTranslucent", new Class[] {translucentConversionListenerClazz});
                method.setAccessible(true);
                method.invoke(activity, new Object[]{proxy});
            } else {
                Method method = Activity.class.getDeclaredMethod("convertToTranslucent", new Class[] {translucentConversionListenerClazz , ActivityOptions.class});
                method.setAccessible(true);
                method.invoke(activity, new Object[]{proxy ,null});
            }

        } catch (Throwable t) {
            LogUtil.e(TAG, Log.getStackTraceString(t));
        }
    }

}

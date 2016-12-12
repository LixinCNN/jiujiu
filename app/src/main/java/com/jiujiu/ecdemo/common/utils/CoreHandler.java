package com.jiujiu.ecdemo.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


public class CoreHandler extends Handler {


    private static int HANDLER_WHAT;

    private final int mWhat;


    private long mDelayMillis = 0L;
    private final boolean mHandle;
    private final HandlerCallbck mHandlerCallbck;


    public CoreHandler(Looper looper , HandlerCallbck callbck , boolean handle) {
        super(looper);
        mWhat = createWhat();
        mHandlerCallbck = callbck;
        mHandle = handle;
    }

    public CoreHandler(HandlerCallbck callbck , boolean handle) {
        mWhat = createWhat();
        mHandlerCallbck = callbck;
        mHandle = handle;
    }


    private static int createWhat() {
        if(HANDLER_WHAT > 8192) {
            HANDLER_WHAT = 0;
        }
        HANDLER_WHAT += 1;
        return HANDLER_WHAT;
    }


    public void removeMessages(){
        removeMessages(mWhat);
    }

    public boolean hasMessages(){
        return hasMessages(mWhat);
    }

    public void sendEmptyMessageDelayed(long delayMillis) {
        mDelayMillis = delayMillis;
        removeMessages();
        sendEmptyMessageDelayed(mWhat, delayMillis);

    }

    @Override
    protected void finalize() throws Throwable {
        removeMessages();
        super.finalize();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what != mWhat && mHandlerCallbck == null) {
            return ;
        }

        if(!mHandlerCallbck.dispatchMessage() || !mHandle) {
            return ;
        }

        sendEmptyMessageDelayed(mWhat, mDelayMillis);
    }


    public interface HandlerCallbck {
        public abstract boolean dispatchMessage();
    }
}

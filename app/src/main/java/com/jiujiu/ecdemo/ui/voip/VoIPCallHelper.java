package com.jiujiu.ecdemo.ui.voip;

import com.jiujiu.ecdemo.common.CCPAppManager;
import com.jiujiu.ecdemo.common.utils.ECNotificationManager;
import com.jiujiu.ecdemo.common.utils.LogUtil;
import com.jiujiu.ecdemo.core.ClientUser;
import com.jiujiu.ecdemo.ui.SDKCoreHelper;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPCallManager.CallBackEntity;
import com.yuntongxun.ecsdk.ECVoIPCallManager.CallType;
import com.yuntongxun.ecsdk.ECVoIPCallManager.OnMakeCallBackListener;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.VoIPCallUserInfo;
import com.yuntongxun.ecsdk.VoipMediaChangedInfo;
/*
* 呼叫事件控制器*/
public class VoIPCallHelper implements OnMakeCallBackListener {

    private static final String TAG = "ECSDK_Demo.VoIPCallHelper";
    private static VoIPCallHelper ourInstance =new VoIPCallHelper() ;

    public static VoIPCallHelper getInstance() {
        return ourInstance;
    }

	/** SDK VoIP呼叫事件通知回调接口 */
	private ECVoIPCallManager mCallInterface;
	private ECVoIPSetupManager mCallSetInterface;
	/** SDK VoIP呼叫接口 */
	private SubVoIPCallback mVoIPCallback;
	/** 用户VoIP通话界面通知接口 */
	private OnCallEventNotifyListener mOnCallEventNotifyListener;
	/** 当前正在进行的VoIP通话信息 */
	private ECVoIPCallManager.VoIPCall mCallEntry;
	/** 是否正在通话 */
	private boolean isCalling = false;
	public static boolean mHandlerVideoCall = false;
	protected VoIPCallUserInfo mUserInfo;
	
	private VoIPCallHelper() {
		mVoIPCallback = new SubVoIPCallback();
        mUserInfo = new VoIPCallUserInfo();
        final ClientUser clientUser = CCPAppManager.getClientUser();
        if(clientUser != null) {
            LogUtil.d(TAG , "username " + clientUser.getUserName() + " , userphone " + clientUser.getUserId());
            mUserInfo.setNickName(clientUser.getUserName());
            mUserInfo.setPhoneNumber(clientUser.getUserId());
        }
	}

    public void stop(){

    }

    /**
     * 发起一个VoIP呼叫
     * @param callType 呼叫类型（音视频、落地）
     * @param number 呼叫号码
     */
    public static String makeCall(ECVoIPCallManager.CallType callType , String number) {
        initCall();
        if(getInstance().mCallInterface == null) {
            LogUtil.e(TAG , "make call error : ECVoIPCallManager null");
            return null;
        }
        if(getInstance().mCallSetInterface != null) {
            getInstance().mCallSetInterface.setVoIPCallUserInfo(getInstance().mUserInfo);
        }
        return getInstance().mCallInterface.makeCall(callType , number);
    }

    public static void makeCallBack(ECVoIPCallManager.CallType callType , String number) {
        initCall();
        if(getInstance().mCallInterface == null) {
            LogUtil.e(TAG , "make call error : ECVoIPCallManager null");
            return ;
        }
        CallBackEntity callBackEntity =new CallBackEntity();
        callBackEntity.caller=CCPAppManager.getUserId();
        callBackEntity.callerSerNum=number;
        
        
        
        callBackEntity.called=number;
        callBackEntity.calledSerNum=CCPAppManager.getUserId();
        
         getInstance().mCallInterface.makeCallBack(callBackEntity, getInstance());
    }

    /**
     * 返回SDK静音状态
     * @return 静音状态
     */
    public static boolean getMute(){
        if(getInstance().mCallSetInterface == null) {
            LogUtil.e(TAG , "get mute error : CallSetInterface null");
            return false;
        }
        return getInstance().mCallSetInterface.getMuteStatus();
    }

    /**
     * 返回SDK免提状态
     * @return 免提状态
     */
    public static boolean getHandFree(){
        if(getInstance().mCallSetInterface == null) {
            LogUtil.e(TAG , "get hand free error : CallSetInterface null");
            return false;
        }
        return getInstance().mCallSetInterface.getLoudSpeakerStatus();
    }

    /**
     * 切换SDK静音状态
     */
	public static void setMute() {
		initCall();
		if (getInstance().mCallSetInterface == null) {
            LogUtil.e(TAG , "set mute error : CallSetInterface null");
            return ;
		}
		getInstance().mCallSetInterface .setMute(!getInstance().mCallSetInterface.getMuteStatus());
	}
	
	
	public static void releaseMuteAndHandFree(){
		initCall();
		if (getInstance().mCallSetInterface == null) {
            LogUtil.e(TAG , "set mute error : CallSetInterface null");
            return ;
		}
		getInstance().mCallSetInterface .setMute(false);
		getInstance().mCallSetInterface.enableLoudSpeaker(false);
		
	}

    /**
     * 切换SDK免提状态
     */
	public static void setHandFree() {

		initCall();
		if (getInstance().mCallInterface == null) {
            LogUtil.e(TAG , "set hand free error : CallSetInterface null");
            return ;
		}
		int r = getInstance().mCallSetInterface .enableLoudSpeaker(!getInstance().mCallSetInterface.getLoudSpeakerStatus());
	}

	/**
	 * 释放通话
	 * 
	 * @param callId
	 *            通话唯一标识
	 */
	public static void releaseCall(String callId) {
		initCall();
		if (getInstance().mCallInterface == null) {
			LogUtil.e(TAG, "release call error : ECVoIPCallManager null");
			return;
		}
		getInstance().mCallInterface.releaseCall(callId);
	}

    /**
     * 接听来电
     * @param callId 通话唯一标识
     */
    public static void acceptCall(String callId) {
        initCall();
        if(getInstance().mCallInterface == null) {
            LogUtil.e(TAG , "accept call error : ECVoIPCallManager null");
            return ;
        }
        getInstance().mCallInterface.acceptCall(callId);
    }

    /**
     * 拒接来电
     * @param callId 通话唯一标识
     */
    public static void rejectCall(String callId) {
        initCall();
        if(getInstance().mCallInterface == null) {
            LogUtil.e(TAG , "reject call error : ECVoIPCallManager null");
            return ;
        }
        // 3 主动拒接
        getInstance().mCallInterface.rejectCall(callId , SdkErrorCode.REMOTE_CALL_BUSY);
    }

	/**
	 * 初始化呼叫控制器
	 */
	public static void initCall() {
		if (ourInstance == null) {
			return;
		}
        ourInstance.mCallInterface = SDKCoreHelper.getVoIPCallManager();
        ourInstance.mCallSetInterface = SDKCoreHelper.getVoIPSetManager();

		if (ourInstance.mCallInterface != null) {
			ourInstance.mCallInterface.setOnVoIPCallListener(getInstance().mVoIPCallback);
		}
        if(ourInstance.mCallSetInterface != null) {
            ClientUser clientUser = CCPAppManager.getClientUser();
            if(clientUser != null) {
                // 设置呼叫参数信息[呼叫昵称、呼叫手机号]
                VoIPCallUserInfo info = new VoIPCallUserInfo(clientUser.getUserName() , clientUser.getUserId());
                ourInstance.mCallSetInterface.setVoIPCallUserInfo(info);
                getInstance().mUserInfo=info;
            }
        }
	}

    /**
     * 设置通话界面刷新通知接口
     * @param callback OnCallEventNotifyListener
     */
    public static void setOnCallEventNotifyListener(OnCallEventNotifyListener callback) {
        getInstance().mOnCallEventNotifyListener = callback;
        initCall();
    }

    /**
     * 当前是否正在进行VoIP通话
     * @return 是否通话
     */
    public static boolean isHoldingCall() {
        return getInstance().isCalling;
    }

    public void release() {
        ourInstance = null;
    }

    /**
     * VoIP通话状态通知
     */
    public interface OnCallEventNotifyListener {
        /**
         * 正在连接服务器
         * @param callId 通话的唯一标识
         */
        void onCallProceeding(String callId);
        
        void onMakeCallback(ECError arg0, String arg1, String arg2);

        /**
         * 对方正在振铃
         * @param callId 通话的唯一标识
         */
        void onCallAlerting(String callId);

        /**
         * 对方应答（通话完全建立）
         * @param callId 通话的唯一标识
         */
        void onCallAnswered(String callId);

        /**
         * 呼叫失败
         * @param callId 通话的唯一标识（有可能为Null）
         * @param reason 呼叫失败原因
         */
        void onMakeCallFailed(String callId, int reason);

        /**
         * VoIP通话结束
         * @param callId 通话的唯一标识
         */
        void onCallReleased(String callId);

        void onVideoRatioChanged(VideoRatio videoRatio);
    }

    private class SubVoIPCallback implements  ECVoIPCallManager.OnVoIPListener {

        @Override
        public void onCallEvents(ECVoIPCallManager.VoIPCall voipCall) {
            // 接收VoIP呼叫事件回调
            if(voipCall == null) {
                LogUtil.e(TAG , "handle call event error , voipCall null" );
                return ;
            }
            OnCallEventNotifyListener notifyListener = VoIPCallHelper.this.mOnCallEventNotifyListener;
            if(notifyListener == null) {
                LogUtil.e(TAG , "notify error , notifyListener null");
                return ;
            }
            mCallEntry = voipCall;
            String callId = mCallEntry.callId;
            switch (voipCall.callState) {
                case ECCALL_PROCEEDING:
                    notifyListener.onCallProceeding(callId);
                    break;
                case ECCALL_ALERTING:
                    notifyListener.onCallAlerting(callId);
                    break;
                case ECCALL_ANSWERED:
                    mHandlerVideoCall = false;
                    notifyListener.onCallAnswered(callId);
                    break;
                case ECCALL_FAILED:
                    notifyListener.onMakeCallFailed(callId , mCallEntry.reason);
                    break;
                case ECCALL_RELEASED:
                    mHandlerVideoCall = false;
                    notifyListener.onCallReleased(callId);
                    getInstance().mOnCallEventNotifyListener = null;
                    ECNotificationManager.cancelCCPNotification(ECNotificationManager.CCP_NOTIFICATOIN_ID_CALLING);

                    break;
                default:
                    break;
            }
            isCalling = (voipCall.callState == ECVoIPCallManager.ECCallState.ECCALL_ANSWERED);
        }

        @Override
        public void onMediaDestinationChanged(VoipMediaChangedInfo mediaChangedInfo) {
            if(mediaChangedInfo!=null){
//                ECVoIPCallManager.MediaChangeType type = mediaChangedInfo.getMediaChangeType();
//                if(type== ECVoIPCallManager.MediaChangeType.P2P){
//                    ToastUtil.showMessage("当前是p2p模式");
//                }
            }


        }


        @Override
		public void onDtmfReceived(String arg0, char arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSwitchCallMediaTypeRequest(String arg0, CallType arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSwitchCallMediaTypeResponse(String arg0, CallType arg1) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onVideoRatioChanged(VideoRatio arg0) {
            OnCallEventNotifyListener notifyListener = VoIPCallHelper.this.mOnCallEventNotifyListener;
            if(notifyListener == null) {
                LogUtil.e(TAG , "notify error , notifyListener null");
                return ;
            }
            notifyListener.onVideoRatioChanged(arg0);
		}
    }

	@Override
	public void onMakeCallback(ECError ecError, String caller, String called) {
		OnCallEventNotifyListener notifyListener = VoIPCallHelper.this.mOnCallEventNotifyListener;
		if(notifyListener == null) {
			return ;
		}
		notifyListener.onMakeCallback(ecError, caller, called);
		
	}
}
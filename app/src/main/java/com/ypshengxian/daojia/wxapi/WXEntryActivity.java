package com.ypshengxian.daojia.wxapi;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ZSH
 * @create 2018/3/26
 * @Describe
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, Count.WX_APP_ID, false);
		api.handleIntent(getIntent(), this);
	}




	/**
	 * 微信发送请求到第三方应用时，会回调到该方法
 	 */

	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
			case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
				goToGetMsg();
				LogUtils.e("onReq，goToGetMsg");
				break;
			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//				goToShowMsg((ShowMessageFromWX.Req) req);
				LogUtils.e("onReq，goToShowMsg");
				break;
			case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
//				Toast.makeText(this, "微信", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				LogUtils.e("resp:" + ((SendAuth.Resp) resp).code);
				EventBus.getDefault().post(new MessageEvent(((SendAuth.Resp) resp).code));
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				break;
			default:
				break;
		}
		this.finish();
	}

	private void goToGetMsg() {
//		Intent intent = new Intent(this, GetFromWXActivity.class);
//		intent.putExtras(getIntent());
//		startActivity(intent);
//		finish();
	}

	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		WXMediaMessage wxMsg = showReq.message;
		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

		StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
		msg.append("description: ");
		msg.append(wxMsg.description);
		msg.append("\n");
		msg.append("extInfo: ");
		msg.append(obj.extInfo);
		msg.append("\n");
		msg.append("filePath: ");
		msg.append(obj.filePath);

//		Intent intent = new Intent(this, ShowFromWXActivity.class);
//		intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//		intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//		intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//		startActivity(intent);
		finish();
	}

}

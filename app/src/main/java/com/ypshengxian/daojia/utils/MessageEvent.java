package com.ypshengxian.daojia.utils;

import android.os.Bundle;

/**
 * @author Yan
 */
public class MessageEvent {
	private int code;
	private String message;
	private Bundle mBundle;

	public MessageEvent(int code, Bundle bundle) {
		this.code = code;
		mBundle = bundle;
	}

	public MessageEvent(int code, String messa) {
		this.code = code;
		message = messa;
	}

	public Bundle getBundle() {
		return mBundle;
	}

	public void setBundle(Bundle bundle) {
		mBundle = bundle;
	}

	public MessageEvent(String message) {
		this.message = message;
	}

	public MessageEvent(int code) {
		this.code = code;
	}

	public MessageEvent() {
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
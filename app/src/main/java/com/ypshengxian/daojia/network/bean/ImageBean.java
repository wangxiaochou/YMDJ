package com.ypshengxian.daojia.network.bean;

import com.yanzhenjie.album.AlbumFile;

/**
 * @author ZSH
 * @create 2018/3/27
 * @Describe
 */

public class ImageBean {
	private boolean isNUll;
	private AlbumFile mAlbumFile;

	public boolean isNUll() {
		return isNUll;
	}

	public void setNUll(boolean NUll) {
		isNUll = NUll;
	}

	public AlbumFile getAlbumFile() {
		return mAlbumFile;
	}

	public void setAlbumFile(AlbumFile albumFile) {
		mAlbumFile = albumFile;
	}
}

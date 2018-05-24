package com.ypshengxian.daojia.network.exception;

/**
 * 会话状态异常
 *
 * @author mos
 * @date 2017.04.24
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class SessionStatusException extends RuntimeException {
    /** 会话状态(已过期) */
    public static final int STATUS_TIMEOUT = 0;
    /** 会话状态(已创建) */
    public static final int STATUS_CREATED = 1;
    /** 会话状态(创建失败) */
    public static final int STATUS_CREATE_FAILED = 2;

    /** 会话状态 */
    private int mSessionStatus;

    /**
     * 构造函数
     *
     * @param sessionStatus 会话状态
     */
    public SessionStatusException(int sessionStatus) {
        super();
        mSessionStatus = sessionStatus;
    }

    /**
     * 构造函数
     *
     * @param sessionStatus 会话状态
     * @param message 消息
     */
    public SessionStatusException(int sessionStatus, String message) {
        super(message);
        mSessionStatus = sessionStatus;
    }

    /**
     * 获取会话状态
     *
     * @return 会话状态
     */
    public int getSessionStatus() {

        return mSessionStatus;
    }
}

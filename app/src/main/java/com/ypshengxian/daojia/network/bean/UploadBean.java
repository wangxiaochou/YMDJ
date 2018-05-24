package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

/**
 * 实体
 *
 * @author Yan
 * @date 2018-03-31
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class UploadBean extends BaseModuleApiResult {

    /**
     * id : 34
     * groupId : 7
     * userId : 2
     * mime :
     * size : 1525
     * status : 0
     * createdTime : 2018-03-30T18:15:37+08:00
     * uploadFileId : null
     * file : {}
     * url : http://admin.ypshengxian.com/files/tmp/2018/03-30/181537922c55610079.png?version=8.2.20
     */

    public String id;
    public String groupId;
    public String userId;
    public String mime;
    public String size;
    public String status;
    public String createdTime;
    public Object uploadFileId;
    public FileBean file;
    public String url;

    public static class FileBean {
    }
}

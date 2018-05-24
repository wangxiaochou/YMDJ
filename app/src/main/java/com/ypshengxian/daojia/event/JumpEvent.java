package com.ypshengxian.daojia.event;

/**
 * 跳转界面
 *
 * @author Yan
 * @date 2018-04-01
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class JumpEvent {
    private String tabName;

    public JumpEvent(String tabName) {
        this.tabName = tabName;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}

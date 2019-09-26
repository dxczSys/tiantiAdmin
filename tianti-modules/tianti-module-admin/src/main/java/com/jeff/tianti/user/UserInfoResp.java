package com.jeff.tianti.user;

import java.util.List;

/**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2019/8/23 23:10
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
public class UserInfoResp {
    private String code;
    private String msg;
    private UserData userInfo;
    private List<Object> list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserData getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserData userInfo) {
        this.userInfo = userInfo;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }
}

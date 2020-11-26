package com.vanni.community.util;

import com.vanni.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息， 用于代替Session对象
 */
@Component
public class HostHolder {

    private ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    // 把user信息存入map中，该map粒度为线程
    public void setUser(User user){
        userThreadLocal.set(user);
    }

    // 获取当前线程中的user对象
    public User getUser() {
        return userThreadLocal.get();
    }

    public void clear(){
        userThreadLocal.remove();
    }
}

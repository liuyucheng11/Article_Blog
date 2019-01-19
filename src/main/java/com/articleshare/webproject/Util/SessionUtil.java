package com.articleshare.webproject.Util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Session 监听器
 */
public class SessionUtil {
    public static HttpSession getSession() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return null;
        }
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }

    /**
     * @param key
     * @param value
     */
    public static void setSessionAttribute(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * @param key
     * @return
     */
    public static Object getSessionAttribute(String key) {
        if (getSession() == null) {
            return null;
        }
        return getSession().getAttribute(key);
    }

    public static void removeSession(String key) {
         getSession().removeAttribute(key);
    }


}

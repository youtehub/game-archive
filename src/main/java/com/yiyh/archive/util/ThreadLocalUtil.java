package com.yiyh.archive.util;

import cn.hutool.core.map.MapUtil;
import org.springframework.core.NamedThreadLocal;

import java.util.Map;

/**
 * Description:
 */
public class ThreadLocalUtil {

    public static final String THREAD_LOCAL_KEY = "GAME-ARCHIVE-THREADLOCAL";

    private static final ThreadLocal<Map<String, Object>> threadLocal = new NamedThreadLocal("game-archive-threadLocal") {
        @Override
        protected Map<String, Object> initialValue() {
            return MapUtil.newHashMap();
        }
    };

    public static Map<String, Object> getThreadLocal() {
        return threadLocal.get();
    }

    public static <T> T get(String key) {
        Map map = threadLocal.get();
        return (T) map.get(key);
    }

    public static <T> T get(String key, T defaultValue) {
        Map map = threadLocal.get();
        return (T) map.get(key) == null ? defaultValue : (T) map.get(key);
    }

    public static void set(String key, Object value) {
        Map map = threadLocal.get();
        map.put(key, value);
    }

    public static void set(Map<String, Object> keyValueMap) {
        Map map = threadLocal.get();
        map.putAll(keyValueMap);
    }

    public static void remove() {
        threadLocal.remove();
    }

}

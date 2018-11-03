package com.darklycoder.srouter;

import com.darklycoder.srouter.info.IRouterTable;
import com.darklycoder.srouter.info.RouterMetaInfo;

import java.util.HashMap;

/**
 * 路由表
 *
 * @author DarklyCoder 2018/11/2
 */
public class RouterTable {

    private static HashMap<String, RouterMetaInfo> routerTable = new HashMap<>();

    /**
     * 自动注入
     */
    static void insertRouterTable(IRouterTable iRouterTable) {
        if (null != iRouterTable) {
            routerTable.putAll(iRouterTable.getRouterTable());
        }
    }

    private RouterTable() {
    }

    private static class SingletonInner {
        private static RouterTable _instance = new RouterTable();
    }

    public static RouterTable getInstance() {
        return SingletonInner._instance;
    }

    public HashMap<String, RouterMetaInfo> getRouterTable() {
        return routerTable;
    }

}

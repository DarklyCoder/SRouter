package com.darklycoder.srouter.annotation.info;

import java.util.HashMap;

/**
 * 路由表
 *
 * @author DarklyCoder 2018/11/1
 */
public interface RouterTable {

    HashMap<String, RouterMetaInfo> getRouterTable();
}

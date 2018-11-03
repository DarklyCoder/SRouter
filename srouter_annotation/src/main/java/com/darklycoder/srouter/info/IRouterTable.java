package com.darklycoder.srouter.info;

import java.util.HashMap;

/**
 * 路由表
 *
 * @author DarklyCoder 2018/11/1
 */
public interface IRouterTable {

    HashMap<String, RouterMetaInfo> getRouterTable();
}

package com.darklycoder.srouter.annotation.info;

/**
 * 元数据
 *
 * @author DarklyCoder 2018/11/1
 */
public class RouterMetaInfo {

    public String route;//路由

    public String path;//执行路径

    public int extra = -1;

    public RouterMetaInfo() {
    }

    public RouterMetaInfo(String route, String path, int extra) {
        this.route = route;
        this.path = path;
        this.extra = extra;
    }

}

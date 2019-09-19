package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImageDao {
    /**
     * 根据线路route的id 去查询图片
     */
    public List<RouteImg> findByRid(int rid);
}

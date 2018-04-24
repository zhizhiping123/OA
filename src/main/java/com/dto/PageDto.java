package com.dto;

import java.security.KeyStore;

/**
 * @Author:EdenJia
 * @Date：create in 9:27 2017/10/9
 * @Describe: 分页展示
 */
public class PageDto {

    private int pageSize;
    private int pageIndex;
    private int totalPage;
    private Object obj;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}

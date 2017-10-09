package com.yy.yjbo.yjboimagecanvas.freeview.bean;

import java.util.List;

/**
 * Created by yjbo on 17/9/18.
 */

public class FreeHeadItemVo {
    private String headernam;
    private String style;
    private List<FreeItemviewVo> itemlist;

    public FreeHeadItemVo() {
    }

    public FreeHeadItemVo(String headernam, String style) {
        this.headernam = headernam;
        this.style = style;
    }

    public FreeHeadItemVo(String headernam, String style, List<FreeItemviewVo> itemlist) {
        this.headernam = headernam;
        this.style = style;
        this.itemlist = itemlist;
    }

    public String getHeadernam() {
        return headernam;
    }

    public void setHeadernam(String headernam) {
        this.headernam = headernam;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<FreeItemviewVo> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<FreeItemviewVo> itemlist) {
        this.itemlist = itemlist;
    }

}

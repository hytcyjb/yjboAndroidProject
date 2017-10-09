package com.yy.yjbo.yjboimagecanvas.freeview.bean;

/**
 * Created by Administrator on 2017/9/17.
 */
//"itemkey": "字段唯一标识",
//"itemname": "客户编码",
//"itemtype": "string",
//"iseditbale": "Y",
//"isrequired": "Y",
//"ismultiselect": "",
//"precision": "",
//"isdisplay": "Y",
//"maxlenth": "",
//"referto": "",
//"editformula": "",
//"style": ""
public class FreeItemviewVo {
    private String itemkey;
    private String itemname;
    private String itemtype;
    private String iseditbale;
    private String isrequired;
    private String ismultiselect;
    private String precision;
    private String isdisplay;
    private String maxlenth;
    private String referto;
    private String editformula;
    private String style;

    public FreeItemviewVo() {
    }

    public FreeItemviewVo(String itemkey, String itemname, String itemtype) {
        this.itemkey = itemkey;
        this.itemname = itemname;
        this.itemtype = itemtype;
    }

    public String getItemkey() {
        return itemkey;
    }

    public void setItemkey(String itemkey) {
        this.itemkey = itemkey;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getIseditbale() {
        return iseditbale;
    }

    public void setIseditbale(String iseditbale) {
        this.iseditbale = iseditbale;
    }

    public String getIsrequired() {
        return isrequired;
    }

    public void setIsrequired(String isrequired) {
        this.isrequired = isrequired;
    }

    public String getIsmultiselect() {
        return ismultiselect;
    }

    public void setIsmultiselect(String ismultiselect) {
        this.ismultiselect = ismultiselect;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getIsdisplay() {
        return isdisplay;
    }

    public void setIsdisplay(String isdisplay) {
        this.isdisplay = isdisplay;
    }

    public String getMaxlenth() {
        return maxlenth;
    }

    public void setMaxlenth(String maxlenth) {
        this.maxlenth = maxlenth;
    }

    public String getReferto() {
        return referto;
    }

    public void setReferto(String referto) {
        this.referto = referto;
    }

    public String getEditformula() {
        return editformula;
    }

    public void setEditformula(String editformula) {
        this.editformula = editformula;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "FreeItemviewVo{" +
                "itemkey='" + itemkey + '\'' +
                ", itemname='" + itemname + '\'' +
                ", itemtype='" + itemtype + '\'' +
                ", iseditbale='" + iseditbale + '\'' +
                ", isrequired='" + isrequired + '\'' +
                ", ismultiselect='" + ismultiselect + '\'' +
                ", precision='" + precision + '\'' +
                ", isdisplay='" + isdisplay + '\'' +
                ", maxlenth='" + maxlenth + '\'' +
                ", referto='" + referto + '\'' +
                ", editformula='" + editformula + '\'' +
                ", style='" + style + '\'' +
                '}';
    }
}

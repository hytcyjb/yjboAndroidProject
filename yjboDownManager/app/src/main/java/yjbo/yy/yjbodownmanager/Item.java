package yjbo.yy.yjbodownmanager;

/**
 * Created by admin on 2017/4/1.
 */

public class Item {
    private int res;
    private String tv1;
    private String tv2;
    private int type;//类型
    private String iconUrl;
    private String downUrl;
    private int downKind;//下载的文件类型：1--apk；2--mp4；

    public Item(int res, String tv1, String tv2) {
        this.res = res;
        this.tv1 = tv1;
        this.tv2 = tv2;
    }

    public Item(int res, String tv1, int type) {
        this.res = res;
        this.tv1 = tv1;
        this.type = type;
    }
    public Item(int res, String tv1, String tv2, int type) {
        this.res = res;
        this.tv1 = tv1;
        this.type = type;
        this.tv2 = tv2;
    }

    public Item(String tv1, int type, String iconUrl, String downUrl) {
        this.tv1 = tv1;
        this.type = type;
        this.iconUrl = iconUrl;
        this.downUrl = downUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getTv1() {
        return tv1;
    }

    public void setTv1(String tv1) {
        this.tv1 = tv1;
    }

    public String getTv2() {
        return tv2;
    }

    public void setTv2(String tv2) {
        this.tv2 = tv2;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public int getDownKind() {
        return downKind;
    }

    public void setDownKind(int downKind) {
        this.downKind = downKind;
    }
}

package com.yy.yjbo.yjbodownmanager2.downUtil.entity;
/**
 * 正在下载的知识点实体
 *
 * Created by gsd on 2014/10/20 0020.
 */
public class DownloadItem {
    /**知识点id*/
    private int downloadId;
    /**知识点标题*/
    private String title;
    /**
     * 下载状态
     */
    private int state;
    /**下载知识点的缩略图*/
    private String imgUrl;
    /**下载日期*/
    private String date;
    /**知识点简介*/
    private String intro;
    /**知识点分类名称*/
    private String categoryName;
    /**知识点所属栏目名称*/
    private String nodeName;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

	/**
	 * 简介
	 * @return
	 */
	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
    
}

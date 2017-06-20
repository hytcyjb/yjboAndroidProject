/**
 * Copyright 2017 bejson.com
 */
package com.zhy.sample_okhttp;

/**
 * Auto-generated: 2017-06-20 17:20:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UserBean {
    /**
     * {"ret":"success","data":"{\"headIcon\":\"zhanghao123/portrait/49d005c8-aa4d-4222-a42c-fe119c2baf9c.jpg\"
     * ,\"accountName\":\"18911966148\",\"openfireDomain\":\"@bagserver\",\"mobileNumber\":\"18911966148\"
     * ,\"companyLogo\":\"89mc/knowledge/img/9ecce9ef-9a52-43e6-a389-3b97b98222c7.png\",\"companyName\":\"八九点测试_zxd\"
     * ,\"userName\":\"贾瑞测试\",\"userID\":\"3400\",\"platform\":\"android\",\"token\":\"6d48d78b-ee22-4ce7-aa1b-3ddbb42097a1\"
     * ,\"companyID\":\"123\",\"companyNO\":\"zhanghao123\",\"imei\":\"869231026460098\",\"openfirePort\":\"5222\",\"account\":\"18911966148\"
     * ,\"timestamp\":\"1497950341772\",\"openfireIP\":\"bag.89mc.com\"}"}
     */
    private String ret;
    private String data;

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getRet() {
        return ret;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "UserBean{" + "\n" +
                "ret='" + ret + '\'' + "\n" +
                ", data='" + data + '\'' + "\n" +
                '}';
    }
}
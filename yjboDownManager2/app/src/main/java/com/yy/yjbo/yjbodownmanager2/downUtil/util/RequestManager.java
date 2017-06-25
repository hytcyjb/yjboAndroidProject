package com.yy.yjbo.yjbodownmanager2.downUtil.util;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.enmc.bag.bean.LoginResult;
import com.enmc.bag.bean.Search;
import com.enmc.enp.main.BagApplication;
import com.enmc.enp.util.Constant;
import com.enmc.enp.util.ConstantValue;
import com.enmc.enp.util.DeviceUtils;
import com.enmc.enp.util.ToastUtils;
import com.enmc.enp.util.sp.SpConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理网络请求
 */
public class RequestManager {
    /**
     * 加积分的类型
     */
    private static final String TYPE = "type";
    /**
     * 超时时间
     */
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;

    private volatile static RequestManager instance;

    private RequestManager() {
    }

    /**
     * <h4>采用双锁机制，安全且在多线程情况下能保持高性能。</h4>
     *
     * @return
     */
    public static RequestManager getInstance() {
        if (instance == null) {
            synchronized (RequestManager.class) {
                if (instance == null) {
                    instance = new RequestManager();
                }
            }
        }
        return instance;
    }

    /**
     * 读取游戏资源
     * @param l
     * @param el
     * @return
     * @throws Exception
     */
   public StringRequest getGameResoource(Listener<String> l, ErrorListener el) throws Exception {
       HashMap<String,String> params = new HashMap<String,String>();
       params.put(ConstantValue.TOKEN_STR,BagApplication.getSPAccount().getToken());
       return getRequest(ConstantValue.GAME_RESOURCE_URL,l,el,params);

   }
    /**
     * 收藏知识点
     * @param kpId 知识点id
     * @param favariteFlag 是否收藏的标签
     * @return
     * @throws Exception
     */
    public StringRequest getKpFavoriteRequest(Listener<String> l, ErrorListener el, int kpId, int favariteFlag)throws Exception {
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("userID", String.valueOf(BagApplication.getSPNormal().getShareIntData(SpConstant.userId)));
        params.put("companyID",BagApplication.getSPAccount().getCompanyId());
        params.put("kpID", String.valueOf(kpId));
        params.put("flag", String.valueOf(favariteFlag));
        return getRequest(ConstantValue.WECHAT_DOMAIN_NAME + "/WeChat/kpCollect.wc",l,el,params);
    }

    /**
     * 知识点评论请求
     * @param kpId 知识点id
     * @param flag 标签 1：点赞；2：点踩
     * @return
     * @throws Exception
     */
    public StringRequest getKpCommentRequest(Listener<String> l, ErrorListener el, int kpId, int flag)throws Exception {
        HashMap<String,String> params = new HashMap<String,String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("kpID", String.valueOf(kpId));
        params.put("flag", String.valueOf(flag));
        return getRequest(ConstantValue.COMMONT_KNOLEDGE,l,el,params);
    }
    /**
     * 判断知识点的标题是否重复
     * @param l
     * @param el
     * @param title 知识点标题
     * @return
     * @throws Exception
     */
    public StringRequest getKnowledgeTitleTestRequest(Listener<String> l, ErrorListener el, String title) throws Exception {
        HashMap<String,String> params = new HashMap<String,String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("title",title);
        return getRequest(ConstantValue.TEST_KNOWLEDGE_TITLE_URL,l,el,params);
    }
    /**
     * 请求卡片内容的公用方法
     * @param l
     * @param el
     * @param compoCode 应用id
     * @return StringRequest
     * @throws Exception
     */
    public StringRequest getCardContentRequest(Listener<String> l, ErrorListener el, String compoCode) throws Exception {
        HashMap<String,String> params = new HashMap<String,String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("compoCode", compoCode);
        return getRequest(ConstantValue.GET_CARD_CONTENT_URL,l,el,params);
    }

    /**
     * 读取重新编辑知识点的信息
     * @param l
     * @param el
     * @param kpId
     * @return
     * @throws Exception
     */
    public StringRequest getKnowledgeInforById(Listener<String> l, ErrorListener el, int kpId) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("kpID", String.valueOf(kpId));
        return getRequest(ConstantValue.KNOWLEDGE_INFOR_URL,l,el,params);
    }

    /** 读取已授权的app列表
     * @param l
     * @param el
     * @return
     * @throws Exception
     */
    public StringRequest getGrantedAppList(Listener<String> l, ErrorListener el)throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        return getRequest(ConstantValue.GET_APP_LIST_URL,l,el,params);
    }
    /**
     * 注销或者退出时请求服务器
     * @param l
     * @param el
     * @return
     * @throws Exception
     */
    public StringRequest getExitRequest(Listener<String> l, ErrorListener el)throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        return getRequest(ConstantValue.EXIT_FROM_SERVER_URL,l,el,params);
    }

    /**
     *  添加或者删除卡片
     * @param l
     * @param el
     * @param flag 标签：添加或者删除
     * @param id 卡片id
     * @return
     * @throws Exception
     */
   public StringRequest addOrDeleteCardRequest(Listener<String> l, ErrorListener el, String flag, int id)throws Exception {
       HashMap<String, String> params = new HashMap<String, String>();
       params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
       params.put("flag",flag);
       params.put("acid", String.valueOf(id));
       return getRequest(ConstantValue.ADD_OR_DELETE_URL,l,el,params);

   }
    /**
     * 发送意见反馈
     * @param l
     * @param el
     * @param feedback 反馈内容
     * @return
     * @throws Exception
     */
    public StringRequest getFeedBackRequest(Listener<String> l, ErrorListener el, String feedback) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("suggestion", feedback);
        return getRequest(ConstantValue.SETTING_FEEDBACK,l,el,params);
    }

    /**
     * 获取积分说明
     * @param l
     * @param el
     * @return
     * @throws Exception
     */
    public StringRequest getIntegralRulesRequest(Listener<String> l, ErrorListener el) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        return getRequest(ConstantValue.GET_INTEGRAL_RULES_URL,l,el,params);
    }
    /**
     * 获取积分等级
     * @return
     * @throws Exception
     */
    public StringRequest getIntegralLevelsRequest(Listener<String> l, ErrorListener el) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        return getRequest(ConstantValue.SCORE_LEVELS_URL,l,el,params);
    }
    /**
     * 设置中修改密码
     * @param oldPwd 加密过的旧密码
     * @param newPwd 加密过的新密码
     * @return
     * @throws Exception
     */
    public StringRequest getChangePasswordRequest(Listener<String> l, ErrorListener el, String oldPwd, String newPwd) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("oldPassword", oldPwd);
        params.put("newPassword", newPwd);
        return getRequest(ConstantValue.SETTING_CHANGEPASSWORD_URL,l,el,params);
    }
    /**
     * 从服务器读取公告的请求
     * @param l
     * @param el
     * @return
     * @throws Exception
     */
    public StringRequest getProclamationListRequest(Listener<String> l, ErrorListener el) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        return getRequest(ConstantValue.MICRO_NOTICE_URL,l,el,params);
    }

    /**
     * 收藏
     * @param kpId 知识点id
     * @return
     * @throws Exception
     */
    public StringRequest getAdd2FavoriteRequest(Listener<String> l, ErrorListener el, Integer kpId) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("kpID", String.valueOf(kpId));
        return getRequest(ConstantValue.ADD_TO_FRAVORITES, l, el, params);
    }

    /**
     * 积分充值
     *
     * @param examId 标杆id
     * @param score  充值积分
     * @return
     * @throws Exception
     */
    public StringRequest createRechargeRequest(Listener<String> l, ErrorListener el, Integer examId, int score, int kpId) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("kpID", String.valueOf(kpId));
        params.put("examID", String.valueOf(examId));
        params.put("score", String.valueOf(score));
        return getRequest(ConstantValue.ADD_SCORE_TO_EXAM_URL, l, el, params);
    }

    /**
     * 新的知识点详情
     *
     * @param l
     * @param el
     * @param kpId
     * @return
     * @throws Exception
     */
    public StringRequest createKpDetailRequest(Listener<String> l, ErrorListener el, Integer kpId) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("kpID", String.valueOf(kpId));
        params.put("adapterSize", String.valueOf(BagApplication.getSPNormal().getAdapterSize()));
        return getRequest(ConstantValue.KP_DETAIL_NEW_URL, l, el, params);
    }

    /**
     * 知识节点列表
     *
     * @param l
     * @param el
     * @param nodeId
     * @return
     * @throws Exception
     */
    public StringRequest createNodeListRequest(Listener<String> l, ErrorListener el, Integer nodeId) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        if (nodeId != null) {
            params.put("nodeID", String.valueOf(nodeId));
        }
        return getRequest(ConstantValue.NODE_CHILD_LIST, l, el, params);
    }

    /**
     * @param l
     * @param el
     * @param pageNum
     * @return StringRequest
     * @throws Exception
     */
    public StringRequest createTotalAwesomeListRequest(Listener<String> l, ErrorListener el, int pageNum) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("pageNumber", String.valueOf(pageNum));
        return getRequest(ConstantValue.TOTAL_INTEGRAL_AWESOME_LIST, l, el, params);
    }

    public StringRequest createStudyAwesomeListRequest(Listener<String> l, ErrorListener el, int pageNum) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("pageNumber", String.valueOf(pageNum));
        return getRequest(ConstantValue.STUDY_AWESOME_LIST, l, el, params);
    }

    public StringRequest createShareAwesomeListRequest(Listener<String> l, ErrorListener el, int pageNum) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("pageNumber", String.valueOf(pageNum));
        return getRequest(ConstantValue.SHARE_AWESOME_LIST, l, el, params);
    }


    /**
     * @param l
     * @param el
     * @param nodeId
     * @param timeStamp
     * @return
     * @throws Exception
     */
    public StringRequest createCompareCacheTimeRequest(Listener<String> l, ErrorListener el, int nodeId, long timeStamp) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("mapID", String.valueOf(nodeId));
        params.put("timeStamp", String.valueOf(timeStamp));

        return getRequest(ConstantValue.COMPARE_CACHE_TIME, l, el, params);

    }

    /**
     * 按关键词搜索的请求
     *
     * @param l           请求正确的监听
     * @param el          请求错误的监听
     * @param jsonBean    关键词
     * @param screenWidth 屏幕宽度
     * @return
     * @throws Exception
     */
    public StringRequest createSearchByKeyWordRequest(Listener<String> l, ErrorListener el, Search jsonBean, int screenWidth) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("adapterSize", String.valueOf(DeviceUtils.getAdapterSize(screenWidth)));
        if (jsonBean.getHotWord() != null && jsonBean.getStudyObjectID() != 0 && jsonBean.getPageNumber() != 0) {
            String jsonString = JSON.toJSONString(jsonBean);
            params.put("paramSearch", jsonString);
        }
        return getRequest(ConstantValue.SEARCH_BY_KEYWORD, l, el, params);

    }

    /**
     * 读取关键词的请求
     *
     * @param l       请求正确的监听
     * @param el      请求错误的监听
     * @param pageNum 页码
     * @return
     * @throws Exception
     */
    public StringRequest createKeyWordListRequset(Listener<String> l, ErrorListener el, int pageNum) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
//        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
//        params.put("pageNumber", String.valueOf(pageNum));
        return getRequest(Constant.getHttpTopIp()+"/WeChat/get_history_word.wc?page_index=1", l, el, params);

    }

    /**
     * 创建读取关注列表的请求
     *
     * @param l           正确结果的监听
     * @param el          错误返回结果的监听
     * @param pageNum     页码
     * @param screenWidth 屏幕宽度
     * @return
     * @throws Exception
     */
    public StringRequest createFocusListRequest(Listener<String> l, ErrorListener el, int pageNum, int screenWidth) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("pageNumber", String.valueOf(pageNum));
        params.put("adapterSize", String.valueOf(DeviceUtils.getAdapterSize(screenWidth)));
        return getRequest(ConstantValue.GET_FOCUS_LISET_URL, l, el, params);

    }

    /**
     * 创建读取收藏列表的请求
     *
     * @param l
     * @param el
     * @param screenWidth
     * @return
     * @throws Exception
     */
    public StringRequest createFavoritesListRequest(Listener<String> l, ErrorListener el, int screenWidth) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("adapterSize", String.valueOf(DeviceUtils.getAdapterSize(screenWidth)));
        return getRequest(ConstantValue.FAVORITES_LIST, l, el, params);

    }

    /**
     * 创建读取学习历史的请求
     *
     * @param l           请求正确的监听
     * @param el          请求错误的监听
     * @param pageNumber  页码
     * @param screenWidth 屏幕宽度
     * @return
     * @throws Exception
     */
    public StringRequest createHistoryListRequest(Listener<String> l, ErrorListener el, int pageNumber, int screenWidth) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("pageNumber", String.valueOf(pageNumber));
        params.put("adapterSize", String.valueOf(DeviceUtils.getAdapterSize(screenWidth)));
        return getRequest(ConstantValue.STUDY_HISTRY_URL, l, el, params);

    }

    /**
     * 首页评论列表请求
     *
     * @param l           请求正确的监听
     * @param el          请求错误的监听
     * @param pageNumber  页码
     * @param screenWinth 屏幕宽度
     * @return
     * @throws Exception
     */
    public StringRequest createReviewListRequest(Listener<String> l, ErrorListener el, int pageNumber, int screenWinth) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("pageNumber", String.valueOf(pageNumber));
        params.put("adapterSize", String.valueOf(DeviceUtils.getAdapterSize(screenWinth)));
        return getRequest(ConstantValue.GETREVIEWLIST, l, el, params);

    }

    /**
     * 按节点和分类形式获取知识点
     *
     * @param l           请求正确监听
     * @param el          请求错误监听
     * @param pageNum     页码
     * @param nodeId      节点id
     * @param categoryId  分类id
     * @param screenWinth 屏幕宽度
     * @return
     * @throws Exception
     */
    public StringRequest createKnowledgeListWithNodeAndCategory(Listener<String> l, ErrorListener el, int pageNum, int nodeId,
                                                                int categoryId, int screenWinth) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("pageNumber", String.valueOf(pageNum));
        params.put("nodeID", String.valueOf(nodeId));
        params.put("categoryID", String.valueOf(categoryId));
        params.put("adapterSize", String.valueOf(DeviceUtils.getAdapterSize(screenWinth)));
        return getRequest(ConstantValue.GET_KP_LIST_BY_NODE_AND_CATEGORY, l, el, params);
    }

    /**
     * 学习列表加载更多（不包含推荐列表）
     *
     * @param l           正确的返回结果监听
     * @param el          错误返回结果监听
     * @param pageNum     页码
     * @param order       排序方式
     * @param roleId      知识库id
     * @param flowerIDS   推荐列表知识点的id
     * @param screenWinth 屏幕宽度
     * @return
     * @throws Exception
     */
    public StringRequest createKnowledgeNoFlowerRequest(Listener<String> l, ErrorListener el, int pageNum, int order, int roleId, String flowerIDS, int screenWinth) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("pageNumber", String.valueOf(pageNum));
        params.put("order", String.valueOf(order));
        params.put("nodeID", String.valueOf(roleId));
        params.put("flowerIDS", flowerIDS);
        params.put("adapterSize", String.valueOf(DeviceUtils.getAdapterSize(screenWinth)));

        return getRequest(ConstantValue.LOAD_MORE_KP_LIST_URL, l, el, params);

    }

    /**
     * 学习列表首页加载更多
     *
     * @param pageNum 页码
     * @param order   排序方式
     * @param nodeID  角色id
     * @return
     * @throws Exception
     */
    public StringRequest createKnowledgeWithFlowerListRequest(Listener<String> l, ErrorListener el,
                                                              int pageNum, int order, int nodeID, int screenWinth) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        params.put("pageNumber", String.valueOf(pageNum));
        params.put("order", String.valueOf(order));
        params.put("nodeID", String.valueOf(nodeID));
        params.put("adapterSize", String.valueOf(DeviceUtils.getAdapterSize(screenWinth)));

        return getRequest(ConstantValue.GET_KNOWLEDGE_AND_FLOWERSLIST_URL, l, el, params);

    }

    /**
     * 获取增加积分的请求<br>
     * <ul>
     * type:类型
     * </ul>
     * <li>1:转发到微信</li> <li>2:转发到群组(伙伴圈)</li> <li>3:游戏玩一玩</li> <li>4:下载文档</li>
     * <li>6:点赞（鲜花）</li> <li>7:点踩（鸡蛋）</li>
     *
     * @param l    请求成功的回调接口
     * @param el   请求错误的回调接口
     * @param type 增加积分的类型
     * @param authorID 知识点点赞时传入，用于给作者增加积分
     * @return
     * @throws Exception
     */
    public StringRequest getAddScoreRequest(Listener<String> l, ErrorListener el, int type, Integer authorID)
            throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        map.put(TYPE, String.valueOf(type));
        if (authorID != null){
            map.put("authorID", String.valueOf(authorID));
        }
        return getRequest(ConstantValue.ADD_SCORE, l, el, map);
    }

    /**
     * 获取节点列表的请求
     *
     * @param l  成功监听
     * @param el 失败监听
     * @return
     * @throws Exception
     */
    public StringRequest getNodelistRequest(Listener<String> l, ErrorListener el) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("studyObjectID", String.valueOf(BagApplication.getSPNormal().getRoleCode()));
        return getRequest(ConstantValue.NODE_LIST_URL, l, el, params);
    }

    /**
     * 获取分类列表
     *
     * @param l  成功监听
     * @param el 失败监听
     * @return
     * @throws Exception
     */
    public StringRequest getCategoryListRequest(Listener<String> l, ErrorListener el) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", BagApplication.getSPAccount().getToken());
        return getRequest(ConstantValue.CATEGORY_LIST_URL, l, el, params);
    }

    /**
     * 获取用户基本信息
     *
     * @param l  成功监听
     * @param el 失败监听
     * @return
     * @throws Exception
     */
    public StringRequest getPersonalBaseInfo(Listener<String> l, ErrorListener el) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", BagApplication.getSPAccount().getToken());
        return getRequest(ConstantValue.GETUSERBASEINFO, l, el, params);
    }

    /**
     * 获取无限级知识点及菜单
     *
     * @param l          监听
     * @param el         错误监听
     * @param nodeId     nodeID
     * @param categoryId categoryId
     * @param pageNumber 页码
     */
    public StringRequest getInfinitiKpRequest(Listener<String> l, ErrorListener el, int nodeId, int categoryId, int pageNumber) throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("mapID", String.valueOf(nodeId));
        params.put("categoryID", String.valueOf(categoryId));
        params.put("pageNumber", String.valueOf(pageNumber));
        return getRequest(ConstantValue.INFINITI_KP_URL, l, el, params);
    }

    /**
     * 获取组织结构信息
     *
     * @param l  结果监听
     * @param el 错误监听
     */
    public StringRequest getOrgInfoByUser(Listener<String> l, ErrorListener el) throws Exception {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", BagApplication.getSPAccount().getToken());
        return getRequest(ConstantValue.DOMAIN_NAME + "/getOrgInfoByUser.mob", l, el, params);
    }

    /**
     * 获取分享知识点列表
     *
     * @param orgID      组织id
     * @param order      排序规则：【1-发布时间2-热度】
     * @param pageNumber 页码
     */
    public StringRequest getKnowledgeListForOrg(int orgID, int order, int pageNumber, int screenWidth, Listener<String> l, ErrorListener el) throws Exception {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("orgID", String.valueOf(orgID));
        params.put("order", String.valueOf(order));
        params.put("pageNumber", String.valueOf(pageNumber));
        params.put("adapterSize", String.valueOf(screenWidth));
        return getRequest(ConstantValue.DOMAIN_NAME + "/getKP4Org.mob", l, el, params);
    }

    /**
     * 获取完整的组织信息
     */
    public StringRequest getAllOrgMapInfo(String orgID, Listener<String> l, ErrorListener el) throws Exception {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", BagApplication.getSPAccount().getToken());
        if (orgID != null)
            params.put("orgID", orgID);
        return getRequest(ConstantValue.GET_ORG_LIST, l, el, params);
    }

    /**
     * 获取联系人请求
     *
     * @param l  正常监听
     * @param el 错误监听
     * @return 请求
     * @throws Exception
     */
    public StringRequest getContactsList(Listener<String> l, ErrorListener el) throws Exception {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put(ConstantValue.TOKEN_STR, BagApplication.getSPAccount().getToken());
        return getRequest(ConstantValue.IM_INTERFACE_GET_CONTACTS, l, el, params);
    }

    /**
     * 获取按人名搜索
     *
     * @param name       关键词
     * @param pageNumber 页码
     * @param l
     * @param el
     * @return
     * @throws Exception
     */
    public StringRequest getSearchUserRequest(String name, int pageNumber, Listener<String> l, ErrorListener el) throws Exception {
        final HashMap<String, String> params = new HashMap<>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("sName", name);
        params.put("pageNumber", String.valueOf(pageNumber));
        return getRequest(ConstantValue.DOMAIN_NAME + "/getAllBuddyByCID.mob", l, el, params);
    }

    /**
     * 获取分词
     *
     * @param contentText 文本内容
     * @param l
     * @param el
     * @return
     * @throws Exception
     */
    public StringRequest getSplitKeyword(String contentText, Listener<String> l, ErrorListener el) throws Exception {
        final HashMap<String, String> params = new HashMap<>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("content", contentText);
        return getRequest(ConstantValue.DOMAIN_NAME + "/splitKeyword.mob", l, el, params);
    }


    /**
     * 更新知识点
     * @param kpId 知识点id
     * @param fileChange 文件是否变更 1，变更；0未变更
     * @param title 知识点标题
     * @param content 知识点内容
     * @param flag 1-微课，2-微例，3-改善或标杆案例， 4-百问， 5-百科
     * @param type 1：改善案例 2：标杆案例
     * @param problem 百问的问题
     * @param answer 百问的答案
     * @param fileURL 资源拼接路径，以英文;分割结尾，只需要文件名称【包含标杆案例图片】
     * @param contentAgo 改善前描述
     * @param contentAfter 改善后描述
     * @param imgAgo 改善前图片绝对路径
     * @param imgAfter 改善后图片绝对路径
     * @param mapID 知识节点
     * @param exampleID 标杆id
     * @param exampleScore 建议给标杆增加的积分
     * @param isReplace 0：非代替上传 1：代替他人上传
     * @param replaceUserID 被代替人id
     * @param fileUrlHttp 缩略图url
     * @param imgAgoHttp 改善前缩略图url
     * @param imgAfterHttp 改善后url
     * @param keyWords 关键词 以英文“,”分割
     * @param l
     * @param el
     * @return
     * @throws Exception
     */
    public StringRequest getUpdateKnowledgeRequest(int kpId, int fileChange, String title, String content, int flag, int type, String problem, String answer, String fileURL,
                                                   String contentAgo, String contentAfter, String imgAgo, String imgAfter, int mapID , int exampleID , int exampleScore,
                                                   int isReplace , int replaceUserID, String fileUrlHttp, String imgAgoHttp, String imgAfterHttp, String keyWords, Listener<String> l, ErrorListener el) throws Exception {
        final HashMap<String, String> params = new HashMap<>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("kpID", String.valueOf(kpId));
        params.put("title", title);
        params.put("content", content);
        params.put("fileChange", String.valueOf(fileChange));
        params.put("fileUrlHttp",fileUrlHttp);
        params.put("flag", String.valueOf(flag));
        if (fileURL != null)
            params.put("fileURL", fileURL);
        if (flag == 3) {
            params.put("type", String.valueOf(type));
            if (type == 1) {
                //改善案例
                params.put("contentAgo", contentAgo);
                params.put("contentAfter", contentAfter);
                params.put("imgAgo", imgAgo);
                params.put("imgAfter", imgAfter);
                params.put("imgAgoHttp",imgAgoHttp);
                params.put("imgAfterHttp",imgAfterHttp);
            } else if (type == 2) {
                //标杆案例
                params.put("exampleID", String.valueOf(exampleID));
                params.put("exampleScore", String.valueOf(exampleScore));
            }
        }
        params.put("mapID", String.valueOf(mapID));
        if (keyWords != null)
            params.put("keyWords", keyWords);
        //add lxf
        if (isReplace == -1){
            isReplace = 0;
        }
        params.put("isReplace", String.valueOf(isReplace));
        if (isReplace == 1)
            params.put("replaceUserID", String.valueOf(replaceUserID));
        if (problem != null){
            params.put("problem",problem);
        }
        if (answer != null){
            params.put("answer",answer);
        }
        return getRequest(ConstantValue.DOMAIN_NAME + "/shareKnowledgeMoreParamUpdate.mob",l,el,params);
    }

    /**
     * 更新改善案例的方法
     * @param kpId 知识点id
     * @param fileChange 文件是否变更 1，变更；0未变更
     * @param title 知识点标题
     * @param flag improveKP:改善案例; exampleKP:标杆案例
     * @param fileURL 资源拼接路径，以英文;分割结尾，只需要文件名称【包含标杆案例图片】
     * @param imgJson 改善案例的图片信息
     * @param mapID 保存的知识路径
     * @param keyWords 关键词列表
     * @param exampleID 标杆id
     * @param examapleScore 给标杆充值的积分数
     * @param isReplace 是否是代替他人上传,0：非代替上传 1：代替他人上传
     * @param replaceUserID 被代替人的id
     * @param l
     * @param el
     * @return
     * @throws Exception
     */
    public StringRequest getUpdateKp4ImproveCaes(int kpId, int fileChange, String title, String flag, String fileURL, String imgJson,
                                                 int mapID, String keyWords,
                                                 int exampleID, int examapleScore,
                                                 int isReplace, int replaceUserID, Listener<String> l, ErrorListener el) throws Exception {
        final HashMap<String, String> params = new HashMap<>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("kpID", String.valueOf(kpId));
        params.put("fileChange", String.valueOf(fileChange));
        params.put("title", title);
        params.put("flag",flag);
        params.put("imgJson",imgJson);
        if (fileURL != null)
            params.put("fileURL", fileURL);
        if (flag.equals("exampleKP")){
            //标杆案例
            params.put("exampleID", String.valueOf(exampleID));
            params.put("exampleScore", String.valueOf(examapleScore));
        }
        params.put("mapID", String.valueOf(mapID));
        if (keyWords != null)
            params.put("keyWords", keyWords);
        //add lxf
        if (isReplace == -1){
            isReplace = 0;
        }
        params.put("isReplace", String.valueOf(isReplace));
        if (isReplace == 1)
            params.put("replaceUserID", String.valueOf(replaceUserID));
        return getRequest(ConstantValue.DOMAIN_NAME + "/shareKnowledgeByCaseUpdate.mob", l, el, params);
    }

    /**
     * 创建改善案例和标杆案例的新街口
     * @param title 知识点标题
     * @param flag improveKP:改善案例; exampleKP:标杆案例
     * @param fileURL 资源拼接路径，以英文;分割结尾，只需要文件名称【包含标杆案例图片】
     * @param imgJson 改善案例的图片信息
     * @param mapID 保存的知识路径
     * @param keyWords 关键词列表
     * @param exampleID 标杆id
     * @param examapleScore 给标杆充值的积分数
     * @param isReplace 是否是代替他人上传,0：非代替上传 1：代替他人上传
     * @param replaceUserID 被代替人的id
     * @return
     * @throws Exception
     */
    public StringRequest getCreateKpRequest(String title, String flag, String fileURL, String imgJson, int mapID, String keyWords, int exampleID, int examapleScore,
                                            int isReplace, int replaceUserID, Listener<String> l, ErrorListener el)throws Exception {
        final HashMap<String, String> params = new HashMap<>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("title", title);
        params.put("flag",flag);
        params.put("imgJson",imgJson);
        if (fileURL != null)
            params.put("fileURL", fileURL);
        if (flag.equals("exampleKP")){
            //标杆案例
            params.put("exampleID", String.valueOf(exampleID));
            params.put("exampleScore", String.valueOf(examapleScore));
        }
        params.put("mapID", String.valueOf(mapID));
        if (keyWords != null)
            params.put("keyWords", keyWords);
        //add lxf
        if (isReplace == -1){
            isReplace = 0;
        }
        params.put("isReplace", String.valueOf(isReplace));
        if (isReplace == 1)
            params.put("replaceUserID", String.valueOf(replaceUserID));
        return getRequest(ConstantValue.DOMAIN_NAME + "/shareKnowledgeByCase.mob", l, el, params);
    }
    /**
     * 创建知识点接口
     *
     * @param title         标题，不能为空
     * @param flag          标志 1-微课 2-普通微例	3-案例 4-百问 5-百科
     * @param content       知识点内容【包含标杆案例内容和改善措施内容】
     * @param fileURL       资源拼接路径，以英文;分割结尾，只需要文件名称【包含标杆案例图片】
     * @param type          1：改善案例 2：标杆案例
     * @param contentAgo    改善前内容
     * @param contentAfter  改善后内容
     * @param imgAgo        改善前图片绝对路径
     * @param imgAfter      改善后图片绝对路径
     * @param mapID         知识节点ID
     * @param keyWords      关键词 以英文“,”分割
     * @param exampleID     标杆人物ID
     * @param examapleScore 标杆添加分值
     * @param isReplace     1:是替代他人上传；0：不是替代他人上传
     * @param replaceUserID 被代替人的id
     * @param problem       知识点的问题
     * @param answer        知识点的答案
     * @param l
     * @param el
     * @return
     * @throws Exception
     */
    public StringRequest getCreateKp(String title, int flag, String content, String fileURL,
                                     int type, String contentAgo, String contentAfter, String imgAgo, String imgAfter,
                                     int mapID, String keyWords, int exampleID, int examapleScore,
                                     int isReplace, int replaceUserID, String problem, String answer,
                                     Listener<String> l, ErrorListener el) throws Exception {
        final HashMap<String, String> params = new HashMap<>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("title", title);
        params.put("flag", String.valueOf(flag));
        params.put("content", content);
        if (fileURL != null)
            params.put("fileURL", fileURL);
        if (flag == 3) {
            params.put("type", String.valueOf(type));
            if (type == 1) {
                //改善案例
                params.put("contentAgo", contentAgo);
                params.put("contentAfter", contentAfter);
                params.put("imgAgo", imgAgo);
                params.put("imgAfter", imgAfter);
            } else if (type == 2) {
                //标杆案例
                params.put("exampleID", String.valueOf(exampleID));
                params.put("exampleScore", String.valueOf(examapleScore));
            }
        }
        params.put("mapID", String.valueOf(mapID));
        if (keyWords != null)
            params.put("keyWords", keyWords);
        //add lxf
        if (isReplace == -1){
            isReplace = 0;
        }
        params.put("isReplace", String.valueOf(isReplace));
        if (isReplace == 1)
            params.put("replaceUserID", String.valueOf(replaceUserID));
        if (problem != null){
            params.put("problem",problem);
        }
        if (answer != null){
            params.put("answer",answer);
        }

        return getRequest(ConstantValue.DOMAIN_NAME + "/shareKnowledgeMoreParam.mob", l, el, params);
    }

    /**
     * 获取所有上传记录的请求
     *
     * @param pageNumber 页码
     * @param l
     * @param el
     * @return
     * @throws Exception
     */
    public StringRequest getAllUploadedListRequest(int pageNumber, Listener<String> l, ErrorListener el) throws Exception {
        final HashMap<String, String> params = new HashMap<>();
        params.put("token", BagApplication.getSPAccount().getToken());
        params.put("pageNumber", String.valueOf(pageNumber));
        return getRequest(ConstantValue.GET_UPLOAD_HISTORY_URL, l, el, params);
    }

    /**
     * 获取请求
     *
     * @param url    连接
     * @param l      请求成功的回调接口
     * @param el     请求错误的回调接口
     * @param params POST请求参数
     * @return
     * @throws Exception
     */
    private StringRequest getRequest(String url, Response.Listener<String> l,
                                     Response.ErrorListener el, final Map<String, String> params) throws Exception {
        StringRequest request = new StringRequest(Method.POST, url, l, el) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    /**
     * 重新登录请求
     *
     * @return
     * @throws Exception
     */
    public StringRequest getReloginRequest() throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("account", BagApplication.getSPAccount().getAccount());
        params.put("password", BagApplication.getSPAccount().getPassword());
        params.put("imie", BagApplication.getSPAccount().getImei());
        final Listener<String> listener = new Response.Listener<String>() {

            @Override
            public void onResponse(String arg0) {
                try {
                    LoginResult result = JSON.parseObject(arg0, LoginResult.class);
                    if (result != null && result.getResult() == 1) {
                        BagApplication.getSPAccount().setToken(result.getToken());
                    } else {
                        ToastUtils.showShort(BagApplication.getInstance(),"身份验证出错，请重新登录");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        final ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

            }
        };
        return getRequest(ConstantValue.LOGIN_URL, listener, errorListener, params);
    }

    public static final class TYPE_SCORE {
        /**
         * 分享到微信
         */
        public static final int SHARE_TO_WX = 1;
        /**
         * 转发到群组(伙伴圈)
         */
        public static final int SHARE_TO_CIRCLE = 2;
        /**
         * 玩一玩
         */
        public static final int PLAY_GAME = 3;
        /**
         * 下载文档
         */
        public static final int DOWNLOAD_FILE = 4;
        /**
         * 下载知识点
         */
        public static final int DOWNLOAD_KNOWLEDGE = 5;
        /**
         * 鲜花
         */
        public static final int FLOWERS = 6;
        /**
         * 鸡蛋
         */
        public static final int EGGS = 7;

    }
}

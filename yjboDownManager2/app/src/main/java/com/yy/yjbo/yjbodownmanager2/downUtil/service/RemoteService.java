package com.yy.yjbo.yjbodownmanager2.downUtil.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.yy.yjbo.yjbodownmanager2.downUtil.db.DownloadDbHelper;
import com.yy.yjbo.yjbodownmanager2.downUtil.db.DownloadListOperator;
import com.yy.yjbo.yjbodownmanager2.downUtil.util.LogUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 文件下载服务（跨进程）
 * Created by Administrator on 2014/10/15 0015.
 */
public class RemoteService extends Service {
    /**
     * 下载id集合为空
     */
    public static final int IDS_EMPTY = 2;
    /**
     * 任务已存在
     */
    public static final int TASK_EXIST = 3;
    /**
     * 执行任务
     */
    public static final int EXECUTE_TASK = 4;
    /**
     * 任务达到上限
     */
    public static final int TASK_LIMIT = 5;
    /**
     * 更新进度
     */
    public static final int UPDATE_PROGRESS = 6;
    /**
     * 下载状态改变
     */
    public static final int DOWNLOAD_STATE_CHANGE = 7;
    /**
     * 队列大小
     */
    public static final int POOL_SIZE = 50;
    /**
     * 同时执行的线程数量
     */
    public static final int CORE_POOL_SIZE = 3;

    public static final int KEEP_ALIVE_TIME = 30;
    /**
     * 下载状态：未开始
     */
    public static final int DOWNLOAD_STATE_PENDING = 1;
    /**
     * 下载状态：下载中
     */
    public static final int DOWNLOAD_STATE_PROCESSING = 2;
    /**
     * 下载状态：暂停
     */
    public static final int DOWNLOAD_STATE_PAUSE = 3;
    /**
     * 下载状态：失败
     */
    public static final int DOWNLOAD_STATE_FAILED = 4;
    /**
     * 下载状态：完成
     */
    public static final int DOWNLOAD_STATE_COMPLETED = 5;
    /**
     * 下载状态：出错
     */
    public static final int DOWNLOAD_STATE_ERROR = 6;
    /**
     * 错误码：删除文件
     *
     * @author yjbo  @time 2017/2/23 15:38
     */
    public static final int ERROR_CODE_DELETE_FILE = 105;

    final RemoteCallbackList<IRemoteServiceCallback> mCallbacks = new RemoteCallbackList<IRemoteServiceCallback>();
    final ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE, POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    public MyHandler handler;
    int mValue = 0;
    private ArrayList<Integer> ids;
    private NotificationManager notificationManager;
    private SQLiteDatabase db;
    private DownloadDbHelper dbHelper;

    //    private static int mdeletedownloadID = 0;//删除的id
    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        handler = new MyHandler(this);
        ids = new ArrayList<Integer>();
        dbHelper = new DownloadDbHelper(this);
//        mdeletedownloadID = 0;
    }

    /**
     * 获取数据库
     *
     * @return
     */
    private SQLiteDatabase getDb() {
        dbHelper = new DownloadDbHelper(this);
        return dbHelper.getWritableDatabase();
    }

    WakeLock wakeLock;

    private void keepCpuRun() {
        try {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FileDownloadRequire");
            wakeLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseWakeLock() {
        try {
            if (wakeLock != null)
                wakeLock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WifiLock wifiLock;

    /**
     * 保持WiF唤醒
     *
     * @throws Exception
     */
    private void keepWifiAlive() throws Exception {
        @SuppressLint("WifiManagerLeak") final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiLock = wifiManager.createWifiLock("RemoteServiceWifiLock");
        wifiLock.acquire();
    }

    /**
     * 释放WiFi锁
     *
     * @throws Exception
     */
    private void releaseWifilock() throws Exception {
        try {
            if (wifiLock != null) {
                wifiLock.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加下载
     *
     * @param downloadID 下载id
     * @param title      标题
     * @param state      状态
     * @param userid     用户id
     * @param intro
     */
    private void insertDownload(int downloadID, String title, int state, int userid, String imgUri, String intro, String category, String node) {
        LogUtils.d("==insertDownload==" + downloadID + "==title=" + title + "==state=" + state + "==userid=" +
                userid + "==imgUri=" + imgUri + "==intro=" + intro + "==category=" + category + "==node=" + node);
        //添加 lxf
        userid = 2395;
        if (db == null) {
            db = getDb();
        }
       /* if(!db.isOpen()||db.isReadOnly()){
            db = dbHelper.getWritableDatabase();
        }*/
        try {
            if (!db.isOpen()) {
                db = getDb();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT count(*) FROM download_list WHERE download_id=? and user_id=?;",
                    new String[]{String.valueOf(downloadID), String.valueOf(userid)});//String.valueOf(userid),切换用户下载保存时读取的上个用户的id
            /*cursor.moveToFirst();
            int count = cursor.getInt(0)*/
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() >= 1) {
                DownloadListOperator operator = new DownloadListOperator();
                operator.deleteById(db, userid, downloadID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        try {
            db.execSQL("INSERT INTO download_list (download_id,title,state,user_id,date,img_uri,intro,catagory,node_name) VALUES (?,?,?,?,?,?,?,?,?);",
                    new String[]{String.valueOf(downloadID), title, String.valueOf(state), String.valueOf(userid), String.valueOf(System.currentTimeMillis()), imgUri, intro, category, node});
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新下载状态
     *
     * @param downloadID 下载id
     * @param state      状态
     */
    private void updateState(int downloadID, int state, int userid) {
        if (db == null) {
            return;
        }
        if (!db.isOpen() || db.isReadOnly()) {
//            return;
            db = dbHelper.getWritableDatabase();
        }
        db.execSQL("UPDATE download_list SET state=?,date=? WHERE download_id=? AND user_id=?;", new String[]{String.valueOf(state), String.valueOf(System.currentTimeMillis()), String.valueOf(downloadID), String.valueOf(userid)});
        db.close();
    }

    /**
     * 更新下载状态By Handler
     *
     * @param downloadID 下载id
     * @param state      状态
     */
    private void updateStateHandler(int downloadID, int state) {
        if (handler != null) {
            handler.sendMessage(handler.obtainMessage(DOWNLOAD_STATE_CHANGE, state, 0, downloadID));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        try {
            db = dbHelper.getWritableDatabase();
            //final int userId = intent.getIntExtra("user_id", 0);
            int userId = 2395;
            if (handler != null) {
                handler.setUserID(userId);
            }
            requestQueue = Volley.newRequestQueue(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDownloadBinder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        if (db.isOpen()) {
            db.close();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (db != null && db.isOpen()) {//TODO 捕获异常
            db.close();
        }
        try {
            releaseWakeLock();
            releaseWifilock();
            requestQueue.cancelAll(this);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 远程接口Binder对象
     */
    private final IRemoteService.Stub mDownloadBinder = new IRemoteService.Stub() {

        @Override
        public int download(String uri, String downloadTitle, int downloadId,
                            String img, String intro, String category, String node) throws RemoteException {
            URL[] urls = null;
            if (uri != null) {
                String[] uris = uri.split(",");
                urls = new URL[uris.length];
                int length = uris.length;
                for (int i = 0; i < length; i++) {
                    try {
                        urls[i] = new URL(uris[i]);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (handler != null) {
                if (ids == null) {
                    handler.sendEmptyMessage(IDS_EMPTY);
                } else if (ids.contains(downloadId)) {
                    handler.sendEmptyMessage(TASK_EXIST);
                } else {
                    int size = ids.size();
                    if (size <= POOL_SIZE) {
                        ids.add(downloadId);
                        Message msg = handler.obtainMessage(EXECUTE_TASK, downloadId, 0, downloadTitle);
                        final Bundle bundle = new Bundle();
                        bundle.clear();
                        bundle.putString("img", img);//第一个不一定是缩略图
                        //bundle.putString("img",img);
                        bundle.putSerializable("urls", urls);
                        bundle.putString("intro", intro);
                        bundle.putString("category", category);
                        bundle.putString("node", node);
                        msg.setData(bundle);

                        handler.sendMessage(msg);
                    } else if (size > POOL_SIZE) {
                        handler.sendEmptyMessage(TASK_LIMIT);
                    }
                }

            }
            return 0;
        }

        @Override
        public int delete(int downloadId) throws RemoteException {
            LogUtils.d("删除当前下载的内容：22=delete==" + "==downloadId===" + downloadId);
            if (handler != null) {
                if (ids.contains(downloadId)) {
                    handler.sendMessage(handler.obtainMessage(8, downloadId, 0, ""));
                }
            }
            return 3;
        }

        @Override
        public void registerCallback(IRemoteServiceCallback callBack) throws RemoteException {
            if (callBack != null) {
                mCallbacks.register(callBack);
            }
        }

        @Override
        public void unregisterCallback(IRemoteServiceCallback callBack) throws RemoteException {
            if (callBack != null) {
                mCallbacks.unregister(callBack);
            }
        }

    };
    private RequestQueue requestQueue;


    static class MyHandler extends Handler {
        WeakReference<RemoteService> outer;
        private int userID;

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public MyHandler(RemoteService remoteService) {
            outer = new WeakReference<RemoteService>(remoteService);
        }

        @Override
        public void handleMessage(Message msg) {
            RemoteService outerService = outer.get();
            if (outerService == null) {
                return;
            }
            switch (msg.what) {
                case IDS_EMPTY: {
                    Toast.makeText(outerService, "请重启应用", Toast.LENGTH_SHORT).show();
                }
                break;
                case TASK_EXIST: {
                    Toast.makeText(outerService, "该任务已经存在，请勿重复添加", Toast.LENGTH_SHORT).show();
                }
                break;
                case EXECUTE_TASK: {
                    try {
                        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            return;
                        }
                        Bundle data = msg.getData();
                        String img = data.getString("img");
                        String intro = data.getString("intro");
                        String category = data.getString("category");
                        String node = data.getString("node");
                        URL[] urls = (URL[]) data.getSerializable("urls");
                        MyAsyncTask myAsyncTask = new MyAsyncTask(msg.arg1, outerService, msg.obj, userID, img, intro, category, node);
                        myAsyncTask.executeOnExecutor(outerService.executor, urls);

                        outerService.keepCpuRun();
                        outerService.keepWifiAlive();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                case TASK_LIMIT: {
                    Toast.makeText(outerService, "任务列队已达到上限", Toast.LENGTH_SHORT).show();
                }
                break;
                case UPDATE_PROGRESS: {
                    int N = outerService.mCallbacks.beginBroadcast();
                    Integer id = (Integer) msg.obj;
                    for (int i = 0; i < N; i++) {
                        try {
                            IRemoteServiceCallback broadcastItem = outerService.mCallbacks.getBroadcastItem(i);
                            broadcastItem.updateProgress(msg.arg1, msg.arg2, id);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    outerService.mCallbacks.finishBroadcast();
                }
                break;
                case DOWNLOAD_STATE_CHANGE: {
                    Integer id = (Integer) msg.obj;
                    int N = outerService.mCallbacks.beginBroadcast();
                    for (int i = 0; i < N; i++) {
                        try {
                            outerService.mCallbacks.getBroadcastItem(i).downloadState(msg.arg1, id);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    outerService.mCallbacks.finishBroadcast();
                    try {
                        if (outerService.ids.size() <= 0) {
                            outerService.releaseWakeLock();
                            outerService.releaseWifilock();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (DOWNLOAD_STATE_COMPLETED == msg.arg1) {//下载完成加积分
                            if (outerService.requestQueue == null) {
                                outerService.requestQueue = Volley.newRequestQueue(outerService);
                            }
                            outerService.requestQueue.add(RequestManager.getInstance().getAddScoreRequest(new AddScoreListener(),
                                    new AddScoreErrorListener(), TYPE_SCORE.DOWNLOAD_KNOWLEDGE, null));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                case 8://删除某个视频下载的线程
//                    new MyAsyncTask(outerService, msg.arg1);
                    Toast.makeText(outerService, "删除某个视频下载的线程", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }

    /**
     * 更新进度
     *
     * @param progress
     * @param max
     */
    private void updateProgressHandler(int id, int progress, int max) {
        if (handler == null) {
            return;
        }
        handler.sendMessage(handler.obtainMessage(UPDATE_PROGRESS, progress, max, id));
    }

    static class MyAsyncTask extends AsyncTask<URL, Integer, Boolean> {
        private static final int BYTES_BUFFER_SIZE = 8192;
        /**
         * 错误码：文件找不到
         */
        private static final int ERROR_CODE_FILE_NOT_FOUND = 1;
        /**
         * 错误码：链接超时
         */
        private static final int ERROR_CODE_TIME_OUT = 2;
        /**
         * 错误码：流异常
         */
        private static final int ERROR_CODE_IO = 3;
        /**
         * 错误码：读取文件长度失败
         */
        private static final int ERROR_CODE_GET_FILE_LENGTH = 4;

        /**
         * 错误码：内存卡不足，无法缓存文件
         */
        private static final int ERROR_CODE_STORE_NONE = 106;
        private int error_code = -1;
        private int downloadId;
        private int mdeldownloadId;
        private String savePath;
        private String title;
        private NotificationCompat.Builder mBuilder;
        //        private Notification.Builder mBuilder;
        private RemoteService context;
        private int userID;
        private String img;
        private String intro;
        private String category;
        private String node;

        public MyAsyncTask(int downloadId, RemoteService outer, Object obj, int userID, String img, String intro, String category, String node) {
            this.downloadId = downloadId;
            this.title = (String) obj;
            this.context = outer;
            this.userID = userID;
            this.img = img;
            this.intro = intro;
            this.category = category;
            this.node = node;
            savePath = ConstantValue.KP_IMG_CACHE + downloadId + "/";
            File folder = new File(savePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }

        public MyAsyncTask(RemoteService outer, int mdownloadId) {
            this.context = outer;
            try {
                this.mdeldownloadId = mdownloadId;
                if (context.ids.contains(mdownloadId)) {
                    final Integer integer = Integer.valueOf(mdownloadId)/* new Integer(downloadId)*/;
                    context.ids.remove(context.ids.indexOf(integer));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context.insertDownload(downloadId, title, DOWNLOAD_STATE_PENDING, userID, img, intro, category, node);

            final Intent resultIntent = new Intent(BagApplication.getInstance(), DownloadListActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(BagApplication.getInstance(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder = new NotificationCompat.Builder(context);
//            mBuilder = new Notification.Builder(BagApplication.getInstance());
            mBuilder.setContentTitle("" + title);
            mBuilder.setContentText(String.format("缓存文件(%d/%d)", 0, 0));
            mBuilder.setSmallIcon(R.drawable.icon_logo_notification_small_icon);
            mBuilder.setProgress(0, 0, true);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setAutoCancel(false);
            context.notificationManager.notify(downloadId, mBuilder.build());
        }

        @Override
        protected Boolean doInBackground(URL... urls) {
            try {
                LogUtils.d("删除当前下载的内容：==" + mdeldownloadId + "==downloadId===" + downloadId);
                int count = urls.length;
                int fileSize = 0;
                for (int i = 0; i < count; i++) {
                    fileSize += getFileSizeAtUrl(urls[i]);
                }
                if ((fileSize / 1024) > UtilsLxf.getAvailaleSize()) {
                    error_code = ERROR_CODE_STORE_NONE;
                    return false;
                }
                int loopCount = 0;
                if (fileSize > 0) {//文件长度
                    int totalBytesRead = 0;
                    for (int i = 0; i < count; i++) {
                        URL url = urls[i];
                        LogUtils.d("删除当前下载的内容：11=error_code===url===" + url);
                        URLConnection conn = url.openConnection();
                        conn.setConnectTimeout(getConnectTimeout());
                        conn.setReadTimeout(getReadTimeout());

                        File targetFile = new File(savePath + getFileName(url.getFile()));
                        RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "rw");

                        int startlong = 0;
                        int contentLength = 0;//某个文件长度
                        if (targetFile.exists()) {
//                            targetFile.delete();
                            startlong = (int) targetFile.length();
                            contentLength = getFileSizeAtUrl(url);
                            LogUtils.d("---从什么地方开始下载--0===" + "bytes=" + startlong + "=-=" + contentLength);
                            if (startlong != 0 || contentLength != 1) {
                                conn.setRequestProperty("Range", "bytes=" + startlong + "-" + contentLength);//设置下载范围
                                randomAccessFile.seek(startlong);//从什么地方开始下载
                            }
                        }
                        LogUtils.d("---从什么地方开始下载--===" + "bytes=" + startlong + "=-=" + contentLength);

                        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());//输入流--得在设置下载范围后面
//                        FileOutputStream fos = new FileOutputStream(targetFile);//输出流
                        int bytesRead = 0;
                        byte[] bytes = new byte[BYTES_BUFFER_SIZE];
                        while (!isCancelled() && (bytesRead = bis.read(bytes)) != -1) {
                            if (isCancelled()) {
                            }//这个判断的目的就是为了一直在判断是否取消线程了，如果取消则能够及时通知onCancel()方法；如果不一直判断，调用onCancel方法会有延迟

                            totalBytesRead += bytesRead;
                            randomAccessFile.write(bytes, 0, bytesRead);//写文件
                            if (!isCancelled() && loopCount++ % 200 == 0) {//防止Notification更新太频繁
                                publishProgress(i + 1, count, (totalBytesRead+startlong), fileSize);
                                chooseMode();
                            }
//                            LogUtils.d("删除当前下载的内容：9=isCancelled()=" + isCancelled());
                        }
                        try {
                            randomAccessFile.close();
                            bis.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
//                        LogUtils.d("删除当前下载的内容：3=isCancelled()=" + isCancelled());
                        if (isCancelled()) {
                            break;
                        }
                    }
//                    LogUtils.d("删除当前下载的内容：4=isCancelled()=" + isCancelled());
//                    LogUtils.d("删除当前下载的内容：6=isCancelled()=" + isCancelled());
                    return true;
                } else {
                    return false;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                error_code = ERROR_CODE_FILE_NOT_FOUND;
                return false;
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                error_code = ERROR_CODE_TIME_OUT;
                return false;
            } catch (TimeoutException e) {
                error_code = ERROR_CODE_TIME_OUT;
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                error_code = ERROR_CODE_IO;
                return false;
            } catch (GetFileLengthException e) {
                try {
                    e.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                error_code = ERROR_CODE_GET_FILE_LENGTH;
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                error_code = -1;
                return false;
            }
        }

        /**
         * 判断在下载中是否进行了删除或者暂停的操作
         * 注意：
         * *删除：当暂停的时候删除也就不走这边了，走其他地方
         *
         * @author yjbo  @time 2017/2/23 16:05
         */
        private void chooseMode() {
            //判断是否已经被删除--delete
            String downDeleteStr = BagApplication.getSPAccount().getShareStringData(SpConstant.downDeleteStr);
//            LogUtils.d("当前删除的视频为==4=" + downloadId + "-----" + downDeleteStr + "====" +
//                    BagApplication.getSPAccount().getShareStringData(SpConstant.downDeleteStr));
            if (downDeleteStr.indexOf("[" + downloadId + "]") != -1) {//说明该条正在下载的内容需要删除
                cancel(true);
                error_code = RemoteService.ERROR_CODE_DELETE_FILE;
                BagApplication.getSPAccount().setShareStringData(SpConstant.downDeleteStr, downDeleteStr.replace("[" + downloadId + "],", ""));

                LogUtils.d("删除当前下载的内容：2222=0=" + downDeleteStr + "==downDeleteStr=2==" +
                        BagApplication.getSPAccount().getShareStringData(SpConstant.downDeleteStr) + "==error_code==" + error_code);
            }
            //判断是否已经被暂停--pause
            String downPauseStr = BagApplication.getSPAccount().getShareStringData(SpConstant.downPauseStr);
            if (downPauseStr.indexOf("[" + downloadId + "]") != -1) {//说明该条正在下载的内容需要暂停
                cancel(true);
                error_code = RemoteService.DOWNLOAD_STATE_PAUSE;
                BagApplication.getSPAccount().setShareStringData(SpConstant.downPauseStr, downPauseStr.replace("[" + downloadId + "],", ""));
                LogUtils.d("暂停当前下载的内容：2222=1=" + downPauseStr + "==downPauseStr=2==" +
                        BagApplication.getSPAccount().getShareStringData(SpConstant.downPauseStr) + "==error_code==" + error_code);
            }
            LogUtils.d("--正在暂停的内容-1-" + BagApplication.getSPAccount().getShareStringData(SpConstant.downPauseStr)
                    + "====" + BagApplication.getSPAccount().getShareStringData(SpConstant.downPauseStr) + "==error_code==" + error_code);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            context.updateProgressHandler(downloadId, values[2], values[3]);//(%d/%d) , values[0], values[1]
            mBuilder.setContentText(String.format("缓存文件,共%.2fMb", (float) values[3] / (1024 * 1014)));
            mBuilder.setProgress(values[3], values[2], false);
            mBuilder.setContentInfo(String.format("%.2f", (float) values[2] / values[3] * 100) + "%");
            context.notificationManager.notify(downloadId, mBuilder.build());
        }

        /**
         * 引用：http://www.niubb.net/a/2015/11-22/1022534.html
         *
         * @author yjbo  @time 2017/2/22 11:01
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();

            switch (error_code) {

                case RemoteService.ERROR_CODE_DELETE_FILE:
//                    LogUtils.d("删除当前下载的内容：5=error_code=" + error_code + "--downloadId--" + downloadId);
                    context.updateStateHandler(downloadId, ERROR_CODE_DELETE_FILE);
                    removeNofity("提示", title + "删除成功");
                    break;
                case RemoteService.DOWNLOAD_STATE_PAUSE://暂停中
                    context.updateStateHandler(downloadId, DOWNLOAD_STATE_COMPLETED);
                    removeNofity("提示", title + "删除成功");
                    break;
                default:
                    break;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            LogUtils.d("删除当前下载的内容：7=error_code=" + error_code + "==aBoolean==" + aBoolean);
            try {
                if (aBoolean) {
                    context.updateState(downloadId, DOWNLOAD_STATE_COMPLETED, userID);
                    mBuilder.setContentText("缓存成功").setDefaults(Notification.DEFAULT_SOUND);
                    mBuilder.setTicker(title + "缓存成功");
                    mBuilder.setProgress(0, 0, false);
                    mBuilder.setContentInfo("100%");
                    mBuilder.setAutoCancel(true);
                    context.notificationManager.notify(downloadId, mBuilder.build());
                    removeId();
                    context.updateStateHandler(downloadId, DOWNLOAD_STATE_COMPLETED);
                } else {
                    LogUtils.d("删除当前下载的内容：8=error_code=" + error_code);
                    switch (error_code) {
                        case -1:
                            showNofity("缓存出错", "错误码：" + error_code);
                            break;
                        case ERROR_CODE_FILE_NOT_FOUND:
                            showNofity("缓存出错", "资源文件找不到");
                            break;
                        case ERROR_CODE_TIME_OUT:
                            showNofity("缓存出错", "链接超时");
                            break;
                        case ERROR_CODE_IO:
                            showNofity("缓存出错", "读取资源失败");
                            break;
                        case ERROR_CODE_GET_FILE_LENGTH:
                            showNofity("缓存出错", "读取文件长度失败");
                            break;
                        case ERROR_CODE_STORE_NONE:
                            showNofity("提示", "内存卡不足，无法缓存文件");
                            break;
                        default:
                            break;
                    }
                }
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
                context.updateState(downloadId, DOWNLOAD_STATE_ERROR, userID);
                mBuilder.setTicker(title + "缓存出错！");
                mBuilder.setContentText("缓存出错,错误码：-2");
                mBuilder.setProgress(0, 0, false);
                mBuilder.setContentInfo("");
                mBuilder.setAutoCancel(true);
                context.notificationManager.notify(downloadId, mBuilder.build());
                removeId();
                context.updateStateHandler(downloadId, DOWNLOAD_STATE_ERROR);
                cancel(true);
            }

        }

        /**
         * 标题后缀
         *
         * @param tickerSuffix 小提示后缀
         * @param contentText  提示内容
         */
        private void showNofity(String tickerSuffix, String contentText) {
            context.updateState(downloadId, DOWNLOAD_STATE_FAILED, userID);
            mBuilder.setTicker(title + tickerSuffix).setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setContentText(contentText);
            mBuilder.setProgress(0, 0, false);
            mBuilder.setContentInfo("");
            mBuilder.setAutoCancel(true);
            context.notificationManager.notify(downloadId, mBuilder.build());
            removeId();
            context.updateStateHandler(downloadId, DOWNLOAD_STATE_FAILED);
        }

        /**
         * 下载失败、暂停中，移除通知
         *
         * @author yjbo  @time 2017/2/23 15:34
         */
        private void removeNofity(String tickerSuffix, String contentText) {
            context.notificationManager.cancel(downloadId);
            removeId();
            error_code = -1;
        }

        private void removeId() {
            try {
                if (context.ids.contains(downloadId)) {
                    final Integer integer = Integer.valueOf(downloadId)/* new Integer(downloadId)*/;
                    context.ids.remove(context.ids.indexOf(integer));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            MyAsyncTask myAsyncTask = RemoteService.getId;
//            if (myAsyncTask.isCancelled()){
//                myAsyncTask.cancel(true);
//            }
        }

        /**
         * 获取给定URL的文件大小
         *
         * @param url 给定的URL
         * @return 文件大小
         */
        public int getFileSizeAtUrl(URL url) throws GetFileLengthException {
            int filesize = 1;
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept-Encoding", "identity");
                filesize = conn.getContentLength();
                if (filesize < 0) {
                    filesize = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new GetFileLengthException("Can not get file Length from Connection");
            } finally {
                try {
                    if (conn != null)
                        conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return filesize;
        }

        /**
         * 获取链接超时时间
         *
         * @return
         */
        private int getConnectTimeout() {
            return 6 * 1000 * 10 * 5;
        }

        /**
         * 获取读超时时间
         *
         * @return
         */
        private int getReadTimeout() {
            return 6 * 1000 * 10 * 5;
        }

        /**
         * 获取文件名
         *
         * @param file
         * @return
         */
        private String getFileName(String file) throws Exception {
            StringTokenizer st = new StringTokenizer(file, "/");
            while (st.hasMoreTokens()) {
                file = st.nextToken();
            }
            //646e2a4a-c24a-42c1-9f18-364affd73ef9.jpg
            String[] split = file.split("\\.");
            if (split.length == 2) {
                if ("mp4".equals(split[1])) {
                    return split[0];
                }
            }
            return file;
        }

    }

    /**
     * 获取文件长度失败
     */
    public static class GetFileLengthException extends Exception {    //或者继承任何标准异常类
        public GetFileLengthException() {
        }                    //用来创建无参数对象

        public GetFileLengthException(String message) {        //用来创建指定参数对象
            super(message);                             //调用超类构造器
        }

    }

}

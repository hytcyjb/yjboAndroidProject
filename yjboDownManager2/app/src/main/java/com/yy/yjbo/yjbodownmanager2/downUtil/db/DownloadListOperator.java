package com.yy.yjbo.yjbodownmanager2.downUtil.db;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.yy.yjbo.yjbodownmanager2.downUtil.entity.DownloadItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/20 0020.
 */
public class DownloadListOperator {
    /**
     *
     *
     * @param <T>
     */
    public interface RowMapper<T> {
        /**
         *
         * @param cursor
         *            游标
         * @param index
         *            下标索引
         * @return
         */
        T mapRow(Cursor cursor, int index);
    }
    /**
     * 查询
     *
     * @param rowMapper
     * @param sql
     *            开始索引 注:第一条记录索引为0
     *            步长
     * @return
     */
    public <T> List<T> queryForList(SQLiteDatabase dataBase, RowMapper<T> rowMapper, String sql, String[] selectionArgs)throws Exception {
        Cursor cursor = null;
        List<T> list = null;
        try {
            cursor = dataBase.rawQuery(sql, selectionArgs);
            list = new ArrayList<T>();
            while (cursor.moveToNext()) {
                list.add(rowMapper.mapRow(cursor, cursor.getPosition()));
            }
        } finally {
            if(dataBase!=null){
                dataBase.close();
            }
            if(cursor!=null){
                cursor.close();
            }
        }
        return list;
    }
    public List<DownloadItem> getListById(SQLiteDatabase dataBase, int userid)throws Exception {
       List<DownloadItem> list = queryForList(dataBase, new RowMapper<DownloadItem>() {
                    @Override
                    public DownloadItem mapRow(Cursor cursor, int index) {
                        DownloadItem item = new DownloadItem();
                        item.setDownloadId(cursor.getInt(cursor.getColumnIndex("download_id")));
                        item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                        item.setState(cursor.getInt(cursor.getColumnIndex("state")));
                        item.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        item.setImgUrl(cursor.getString(cursor.getColumnIndex("img_uri")));
                        item.setIntro(cursor.getString(cursor.getColumnIndex("intro")));
                        item.setCategoryName(cursor.getString(cursor.getColumnIndex("catagory")));
            			item.setNodeName(cursor.getString(cursor.getColumnIndex("node_name")));
                        return item;
                    }
                },
                "SELECT * FROM download_list WHERE user_id=?;",new String[]{String.valueOf(userid)});
        return  list;
    }

	public List<DownloadItem> getListByIdAndState(SQLiteDatabase dataBase, int userid, int state)throws Exception {
    	List<DownloadItem> list = queryForList(dataBase, new RowMapper<DownloadItem>() {
    		@Override
    		public DownloadItem mapRow(Cursor cursor, int index) {
    			DownloadItem item = new DownloadItem();
    			item.setDownloadId(cursor.getInt(cursor.getColumnIndex("download_id")));
    			item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
    			item.setState(cursor.getInt(cursor.getColumnIndex("state")));
    			item.setDate(cursor.getString(cursor.getColumnIndex("date")));
    			item.setImgUrl(cursor.getString(cursor.getColumnIndex("img_uri")));
    			item.setIntro(cursor.getString(cursor.getColumnIndex("intro")));
    			item.setCategoryName(cursor.getString(cursor.getColumnIndex("catagory")));
    			item.setNodeName(cursor.getString(cursor.getColumnIndex("node_name")));
    			return item;
    		}
    	},
    	"SELECT * FROM download_list WHERE user_id=? AND state=? ORDER BY date DESC;",new String[]{String.valueOf(userid), String.valueOf(state)});
    	return  list;
    }
    public List<DownloadItem> getListByIdAndState(SQLiteDatabase dataBase, int userid, int state, int state2, int state3, int state4, int state5)throws Exception {
    	List<DownloadItem> list = queryForList(dataBase, new RowMapper<DownloadItem>() {
    		@Override
    		public DownloadItem mapRow(Cursor cursor, int index) {
    			DownloadItem item = new DownloadItem();
    			item.setDownloadId(cursor.getInt(cursor.getColumnIndex("download_id")));
    			item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
    			item.setState(cursor.getInt(cursor.getColumnIndex("state")));
    			item.setDate(cursor.getString(cursor.getColumnIndex("date")));
    			item.setImgUrl(cursor.getString(cursor.getColumnIndex("img_uri")));
    			item.setIntro(cursor.getString(cursor.getColumnIndex("intro")));
    			item.setCategoryName(cursor.getString(cursor.getColumnIndex("catagory")));
    			item.setNodeName(cursor.getString(cursor.getColumnIndex("node_name")));
    			return item;
    		}
    	},
    	"SELECT * FROM download_list WHERE (user_id=? AND (state=? OR state=? OR state=? OR state=? OR state=?)) ORDER BY date DESC;",new String[]{String.valueOf(userid), String.valueOf(state), String.valueOf(state2), String.valueOf(state3), String.valueOf(state4), String.valueOf(state5)});
    	return  list;
    }
    private int kpIdIndex = -1;
    public List<Integer> getDownloadIdsByUserIdAndState(SQLiteDatabase dataBase, int userid, int state)throws Exception {
        List<Integer> list = queryForList(dataBase, new RowMapper<Integer>() {
                    @Override
                    public Integer mapRow(Cursor cursor, int index) {
                        if(kpIdIndex==-1)
                            kpIdIndex = cursor.getColumnIndex("download_id");
                        return cursor.getInt(kpIdIndex);
                    }
                },
                "SELECT * FROM download_list WHERE user_id=? AND state=?;",new String[]{String.valueOf(userid), String.valueOf(state)});
        return  list;
    }
    public List<DownloadItem> getListByUseridAndDownloadid(SQLiteDatabase dataBase, int userid, int downloadid)throws Exception {
    	List<DownloadItem> list = queryForList(dataBase, new RowMapper<DownloadItem>() {
    		@Override
    		public DownloadItem mapRow(Cursor cursor, int index) {
    			DownloadItem item = new DownloadItem();
    			item.setDownloadId(cursor.getInt(cursor.getColumnIndex("download_id")));
    			item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
    			item.setState(cursor.getInt(cursor.getColumnIndex("state")));
    			item.setDate(cursor.getString(cursor.getColumnIndex("date")));
    			item.setImgUrl(cursor.getString(cursor.getColumnIndex("img_uri")));
    			item.setIntro(cursor.getString(cursor.getColumnIndex("intro")));
    			item.setCategoryName(cursor.getString(cursor.getColumnIndex("catagory")));
    			item.setNodeName(cursor.getString(cursor.getColumnIndex("node_name")));
    			return item;
    		}
    	},
    	"SELECT * FROM download_list WHERE user_id=? AND download_id=?;",new String[]{String.valueOf(userid), String.valueOf(downloadid)});
    	return  list;
    }
    
    /**
     * @param dataBase
     * @param state 知识点下载状态：5为下载完成
     * @param downloadid 知识点id
     * @return
     * @throws Exception
     */
    public List<DownloadItem> getListByStateAndDownloadid(SQLiteDatabase dataBase, int state, int downloadid) throws Exception {
    	    List<DownloadItem> list = queryForList(dataBase, new RowMapper<DownloadItem>() {
				@Override
				public DownloadItem mapRow(Cursor cursor, int index) {
					DownloadItem item = new DownloadItem();
	    			item.setDownloadId(cursor.getInt(cursor.getColumnIndex("download_id")));
	    			item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
	    			item.setState(cursor.getInt(cursor.getColumnIndex("state")));
	    			item.setDate(cursor.getString(cursor.getColumnIndex("date")));
	    			item.setImgUrl(cursor.getString(cursor.getColumnIndex("img_uri")));
	    			item.setIntro(cursor.getString(cursor.getColumnIndex("intro")));
	    			item.setCategoryName(cursor.getString(cursor.getColumnIndex("catagory")));
	    			item.setNodeName(cursor.getString(cursor.getColumnIndex("node_name")));
	    			return item;
				}
			},
    	    "SELECT * FROM download_list WHERE state=? AND download_id=?;",new String[]{String.valueOf(state), String.valueOf(downloadid)});
    	    return list;
    }
    
    public void deleteById(SQLiteDatabase dataBase, int userid, int downloadID){
         try {
//        	 dataBase.rawQuery("DELETE FROM 'download_list' WHERE user_id=? AND download_id=?;", new String[]{String.valueOf(userid),String.valueOf(downloadID)});
        	 dataBase.delete("download_list", "user_id=? AND download_id=?",  new String[]{String.valueOf(userid), String.valueOf(downloadID)});
         } catch (Exception e){
             e.printStackTrace();
         }
    }
    public int getDownloadCount(SQLiteDatabase database, int userid, int downloadID){
    	Cursor cursor = null;
    	try {
//       	 dataBase.rawQuery("DELETE FROM 'download_list' WHERE user_id=? AND download_id=?;", new String[]{String.valueOf(userid),String.valueOf(downloadID)});
    		cursor = database.rawQuery("SELECT count(*) FROM 'download_list' WHERE user_id=? AND download_id=?;", new String[]{String.valueOf(userid), String.valueOf(downloadID)});
    		return cursor.getCount();
        } finally {
            if(database!=null){
            	database.close();
            }
            if(cursor!=null){
            	cursor.close();
            }
        }
    }
}

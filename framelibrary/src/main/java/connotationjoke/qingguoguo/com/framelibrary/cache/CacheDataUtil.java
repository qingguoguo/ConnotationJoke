package connotationjoke.qingguoguo.com.framelibrary.cache;

import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;
import com.connotationjoke.qingguoguo.baselibrary.util.MD5Util;

import java.util.List;

import connotationjoke.qingguoguo.com.framelibrary.db.DaoSupportFactory;
import connotationjoke.qingguoguo.com.framelibrary.db.IDaoSupport;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/4 21:45
 * @Describe :获取缓存数据工具类
 */
public class CacheDataUtil {
    public static final String TAG = "CacheDataUtil";

    /**
     * 获取缓存数据
     */
    public static String getCacheResultJson(String finalUrl) {
        final IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        // 需要缓存，从数据库拿缓存
        List<CacheData> cacheDatas = dataDaoSupport.querySupport().selection("mUrlKey = ?").selectionArgs(MD5Util.string2MD5(finalUrl)).query();

        if (cacheDatas.size() != 0) {
            //有缓存数据
            CacheData cacheData = cacheDatas.get(0);
            String resultJson = cacheData.getJsonResult();
            return resultJson;
        }
        return null;
    }

    /**
     * 缓存数据
     */
    public static long cacheData(String finalUrl, String resultJson) {
        IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        dataDaoSupport.delete("mUrlKey=?", MD5Util.string2MD5(finalUrl));
        long number = dataDaoSupport.insert(new CacheData(MD5Util.string2MD5(finalUrl), resultJson));
        LogUtils.i(TAG, "number --> " + number);
        return number;
    }
}

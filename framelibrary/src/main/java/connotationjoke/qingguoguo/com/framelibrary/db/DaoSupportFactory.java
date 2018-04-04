package connotationjoke.qingguoguo.com.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/4
 * @describe :要持有外部数据库的引用
 */

public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;
    private SQLiteDatabase sqLiteDatabase;

    private DaoSupportFactory() {
        //数据库放在外部存储卡
        //判断是否有存储卡，6.0+需要动态申请权限
        File dbRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "nhdz" + File.separator + "database");
        if (!dbRoot.exists()) {
            dbRoot.mkdirs();
        }
        File dbFile = new File(dbRoot, "nhdz.db");
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public static DaoSupportFactory getFactory() {
        if (mFactory == null) {
            synchronized (DaoSupportFactory.class) {
                if (mFactory == null) {
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    public <T> IDaoSupport getDao(Class<T> clazz) {
        IDaoSupport<T> daoSupport = new DaoSupport<T>();
        daoSupport.init(sqLiteDatabase,clazz);
        return daoSupport;
    }
}

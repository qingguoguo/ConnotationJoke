package connotationjoke.qingguoguo.com.framelibrary.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;

import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/4
 * @describe :封装自己的数据库
 */

public class DaoSupport<T> implements IDaoSupport<T> {
    private static final String TAG = "DaoSupport";
    private SQLiteDatabase mSQLiteDatabase;
    private Class<?> mClazz;

    private static final Map<String, Method> mPutMethod = new ArrayMap<>();
    private final Object[] mPutMethodArgs = new Object[2];

    @Override
    public void init(SQLiteDatabase SQLiteDatabase, Class<?> clazz) {
        mSQLiteDatabase = SQLiteDatabase;
        mClazz = clazz;

        //创建表
        //create table if not exists Person ("id integer primary key autoincrement, name text, age integer, flag boolean");

        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists ")
                .append(DaoUtil.getTableName(mClazz))
                .append(" (id integer primary key autoincrement,");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            sb.append(field.getName())
                    .append((DaoUtil.getColumnType(field.getType().getSimpleName())))
                    .append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), ")");
        LogUtils.i(TAG, "创建表语句：" + sb.toString());
        mSQLiteDatabase.execSQL(sb.toString());
    }

    @Override
    public long insert(T t) {
        long index = mSQLiteDatabase.insert(DaoUtil.getTableName(mClazz), null, getContentValuesByObj(t));
        return index;
    }

    @Override
    public void insert(List<T> datas) {
        //数据库优化 批量插入开启事务
        mSQLiteDatabase.beginTransaction();
        if (datas != null && !datas.isEmpty()) {
            for (T data : datas) {
                insert(data);
            }
        }
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    public ContentValues getContentValuesByObj(T obj) {
        //ContentValues put方法不接受object类型，每次put都要转类型
        //利用反射来执行put方法
        //每次都利用反射执行，会损耗性能，可以仿照源码优化
        ContentValues values = new ContentValues();
        Class<ContentValues> contentValuesClass = ContentValues.class;

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                mPutMethodArgs[0] = field.getName();
                mPutMethodArgs[1] = field.get(obj);
                String fieldTypeName= field.getType().getName();
                //从缓存中获取Method
                Method putMethod = mPutMethod.get(fieldTypeName);
                if (putMethod== null) {
                    putMethod = contentValuesClass.getDeclaredMethod("put", String.class, mPutMethodArgs[1].getClass());
                    mPutMethod.put(fieldTypeName, putMethod);
                }
                putMethod.invoke(values, mPutMethodArgs);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        return values;
    }
}

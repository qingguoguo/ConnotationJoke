package connotationjoke.qingguoguo.com.framelibrary.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/4
 * @describe :封装自己的数据库
 */

public class DaoSupport<T> implements IDaoSupport<T> {
    private static final String TAG = "DaoSupport";
    private SQLiteDatabase mSQLiteDatabase;
    private Class<?> mClazz;

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
        if (datas != null && !datas.isEmpty()) {
            for (T data : datas) {
                insert(data);
            }
        }
    }

    public ContentValues getContentValuesByObj(T obj) {
        //ContentValues put方法不接受object类型，每次put都要转类型
        //利用反射来执行put方法
        ContentValues values = new ContentValues();
        Class<ContentValues> contentValuesClass = ContentValues.class;

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(obj);
                Method putMethod = contentValuesClass.getDeclaredMethod("put", String.class, value.getClass());
                putMethod.invoke(values, field.getName(), value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return values;
    }
}

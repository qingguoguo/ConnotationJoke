package connotationjoke.qingguoguo.com.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/4
 * @describe :
 */

public interface IDaoSupport<T> {

    void init(SQLiteDatabase SQLiteDatabase, Class<?> clazz);

    /**
     * 插入数据
     *
     * @param t
     * @return
     */
    long insert(T t);

    /**
     * 插入数据
     *
     * @param datas
     */
    void insert(List<T> datas);
}

package connotationjoke.qingguoguo.com.framelibrary.view.recyclerview.adapter;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/13 14:05
 * @Describe :多布局支持接口
 */
public interface MultiTypeSupport<T> {
    /**
     * 根据当前位置或者条目数据返回布局
     *
     * @param item
     * @param position
     * @return
     */
    int getLayoutId(T item, int position);
}

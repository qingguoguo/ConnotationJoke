package connotationjoke.qingguoguo.com.framelibrary.view.selectimage;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import connotationjoke.qingguoguo.com.framelibrary.R;
import connotationjoke.qingguoguo.com.framelibrary.view.recyclerview.adapter.CommonRecyclerAdapter;
import connotationjoke.qingguoguo.com.framelibrary.view.recyclerview.adapter.ViewHolder;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/13 17:05
 * @Describe :图片选择的适配器
 */
public class SelectImageListAdapter extends CommonRecyclerAdapter<String> {
    private ArrayList<String> mResultImageList;
    private int mMaxCount;

    public SelectImageListAdapter(Context context, List<String> data, ArrayList<String> imageList, int maxCount) {
        super(context, data, R.layout.media_chooser_item);
        this.mResultImageList = imageList;
        this.mMaxCount = maxCount;
    }

    @Override
    public void convert(ViewHolder holder, final String item) {
        if (TextUtils.isEmpty(item)) {
            // 显示拍照的图片
            holder.setViewVisibility(R.id.camera_ll, View.VISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator, View.INVISIBLE);
            holder.setViewVisibility(R.id.image, View.INVISIBLE);
            holder.setOnIntemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 拍照权限 http://www.jianshu.com/p/823360bb183f
                    if (mListener != null) {
                        mListener.onCamera();
                    }
                }
            });
        } else {
            //显示图片
            holder.setViewVisibility(R.id.camera_ll, View.INVISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator, View.VISIBLE);
            holder.setViewVisibility(R.id.image, View.VISIBLE);

            //显示图片利用Glide
            ImageView imageView = holder.getView(R.id.image);
            Glide.with(mContext).load(item).centerCrop().into(imageView);
            ImageView selectIndicatorIv = holder.getView(R.id.media_selected_indicator);

            if (mResultImageList.contains(item)) {
                // 点亮选择勾住图片
                selectIndicatorIv.setSelected(true);
            } else {
                selectIndicatorIv.setSelected(false);
            }

            // 给条目增加点击事件
            holder.setOnIntemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 没有就加入集合，有就从集合移除
                    if (!mResultImageList.contains(item)) {
                        // 不能大于最大的张数
                        if (mResultImageList.size() >= mMaxCount) {
                            // 自自定义Toast  文字写在string里面
                            Toast.makeText(mContext, "最多只能选取" + mMaxCount + "张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mResultImageList.add(item);
                    } else {
                        mResultImageList.remove(item);
                    }
                    notifyDataSetChanged();
                    // 通知布局更新
                    if (mListener != null) {
                        mListener.select();
                    }
                }
            });
        }
    }

    /**
     * 设置选择图片监听
     */
    private SelectImageListener mListener;

    public void setOnSelectImageListener(SelectImageListener listener) {
        this.mListener = listener;
    }
}

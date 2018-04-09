package connotationjoke.qingguoguo.com.framelibrary.skin;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import connotationjoke.qingguoguo.com.framelibrary.skin.attr.SkinAttr;
import connotationjoke.qingguoguo.com.framelibrary.skin.attr.SkinType;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/9
 * @describe :皮肤属性解析的支持类
 */

public class SkinAttrSupport {

    /**
     * 获取属性
     *
     * @param context
     * @param attrs
     * @return
     */
    public static List<SkinAttr> getSKinAttrs(Context context, AttributeSet attrs) {
        List<SkinAttr> skinAttrs = new ArrayList<>();
        int attributeCount = attrs.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            //获取属性名称和值
            String attributeName = attrs.getAttributeName(i);
            String attributeValue = attrs.getAttributeValue(i);
            //剔除掉不重要的属性，只拿关心的属性
            SkinType sKinType = getSKinType(attributeName);
            if (sKinType != null) {
                //id转换成资源名称
                String resName = getResName(context, attributeValue);
                if (TextUtils.isEmpty(resName)) {
                    continue;
                }
                SkinAttr skinAttr = new SkinAttr(sKinType, resName);
                skinAttrs.add(skinAttr);
            }
        }
        return skinAttrs;
    }

    /**
     * 通过Id获取资源名字
     *
     * @param context
     * @param attributeValue
     * @return
     */
    private static String getResName(Context context, String attributeValue) {
        if (attributeValue.startsWith("@")) {
            attributeValue = attributeValue.substring(1);
            int resId = Integer.parseInt(attributeValue);
            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    /**
     * 通过属性名字转换成SkinType
     *
     * @param attributeName
     * @return
     */
    private static SkinType getSKinType(String attributeName) {
        SkinType[] values = SkinType.values();
        for (SkinType value : values) {
            if (value.getResName().equals(attributeName)) {
                return value;
            }
        }
        return null;
    }
}

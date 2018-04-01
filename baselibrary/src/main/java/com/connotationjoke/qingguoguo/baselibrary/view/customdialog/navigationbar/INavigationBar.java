package com.connotationjoke.qingguoguo.baselibrary.view.customdialog.navigationbar;

/**
 * 作者:qingguoguo
 * 创建日期：2018/4/1 on 19:43
 * 描述: 定义导航栏接口 规范
 */

public interface INavigationBar {

    /**
     * 布局Id
     *
     * @return
     */
    int bindLayoutResId();


    /**
     * 绑定头部参数
     */
    void applyView();
}

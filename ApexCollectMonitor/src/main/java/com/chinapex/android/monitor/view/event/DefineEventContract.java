package com.chinapex.android.monitor.view.event;

import com.chinapex.android.monitor.view.BasePresenter;
import com.chinapex.android.monitor.view.BaseView;

import java.util.List;

/**
 * @author wyhusky
 * @date 2018/12/27
 */
public interface DefineEventContract {
    interface View extends BaseView {
        /**
         * 获取view类型
         * @return IFloatingView.WINDOW_DEFINE_PV_PAGE, WINDOW_DEFINE_CLICK_PAGE,WINDOW_DEFINE_LIST_PAGE;
         */
        int getViewType();

        /**
         * 设置控件路径
         * @param path 控件的viewPathId
         */
        void showViewPath(String path);

        /**
         * 设置页面引用
         * @param reference 页面引用或是圈选所在页面
         */
        void showReference(String reference);

        /**
         * 加载activiy, fragment页面路径
         * @param pages 定义页面时，加载可选的页面到spinner
         */
        void loadPagePath(List<String> pages);

        /**
         * 设置折线图数据
         * @param clickCounts 点击量
         * @param date 日期 eg.2018-11-30 12:00
         */
        void showLineChart(int[] clickCounts, String[] date);

        /**
         * 弹吐司
         * @param id 资源id
         */
        void showToast(int id);
    }

    interface Presenter extends BasePresenter {
        /**
         * 加载PV事件数据
         */
        void loadPvData();

        /**
         * 更新页面数据
         * @param viewPath 页面的viewPath
         */
        void updatePvData(String viewPath);

        /**
         * 加载点击事件数据
         * @param viewPath 控件路径
         * @param pageClass 控件所在页面路径（fragment或activity）
         */
        void loadClickData(String viewPath, String pageClass);

        /**
         * 加载列表点击事件数据
         * @param viewPath list的唯一路径
         * @param pageClass 所在页面路径
         * @param itemPath 列表项的唯一路径
         */
        void loadListItemClickData(String viewPath, String pageClass, String itemPath);

        /**
         * 保存自定义点击事件
         * @param alias 事件别名
         */
        void saveClickEvent(String alias);

        /**
         * 保存自定义列表项点击事件
         * @param alias 事件别名
         * @param itemPath 列表项条目的路径
         */
        void saveListItemClickEvent(String alias, String itemPath);

        /**
         * 保存自定义页面事件
         * @param alias 事件别名
         * @param definedPage 定义页面的路径，后面会作为点击事件的圈选页面
         */
        void savePageViewEvent(String alias, String definedPage);

        /**
         * 取消定义事件
         */
        void dismiss();
    }
}

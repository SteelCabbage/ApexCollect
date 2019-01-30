package com.chinapex.android.monitor.view.statistics;

import com.chinapex.android.monitor.bean.StatisticsBean;
import com.chinapex.android.monitor.global.MonitorCache;
import com.chinapex.android.monitor.utils.ColorUtils;
import com.chinapex.android.monitor.utils.MLog;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.chinapex.android.monitor.utils.ColorUtils.random;

/**
 * @author wyhusky
 * @date 2019/1/17
 */
public class StatisticsViewPresenter implements StatisticsViewContract.Presenter {

    private static final String TAG = StatisticsViewPresenter.class.getSimpleName();
    private StatisticsViewContract.View mStatisticsView;

    public StatisticsViewPresenter(StatisticsViewContract.View view) {
        mStatisticsView = view;
    }

    @Override
    public void init() {

    }

    @Override
    public void loadContrastData() {
        testData();
        mStatisticsView.updateData();

    }

    private void testData() {
        Set<String> viewPathMD5List = MonitorCache.getInstance().getContrastItems();
        if (viewPathMD5List.isEmpty()) {
            MLog.e(TAG, "testData()-> viewPathMD5 is empty");
            return;
        }
        for (String str : viewPathMD5List) {
            MLog.d(TAG, viewPathMD5List.toString() + "\n");
        }
        List<StatisticsBean> beans = MonitorCache.getInstance().getStatisticsBeans();
        if (null == beans) {
            MLog.e(TAG, "testData()-> beans is null");

            return;
        }
        beans.clear();
        Random random = new Random();
        String[] viewPathMD5Array = viewPathMD5List.toArray(new String[viewPathMD5List.size()]);
        float total = 0;
        for (int i = 0; i < viewPathMD5List.size(); i++) {
            int count = random.nextInt(500);
            StatisticsBean statisticsBean = new StatisticsBean();
            statisticsBean.setClickCount(count);
            statisticsBean.setViewPathMD5(viewPathMD5Array[i]);
            statisticsBean.setEventLabel("点击事件" + i);
            statisticsBean.setColor(ColorUtils.generateColor());
            beans.add(statisticsBean);
            total += count;
        }

        for (StatisticsBean bean : beans) {
            bean.setClickProportion(bean.getClickCount() / total * 100);
        }

        Collections.sort(beans, new Comparator<StatisticsBean>() {
            @Override
            public int compare(StatisticsBean statisticsBean, StatisticsBean t1) {
                if (statisticsBean.getClickCount() == t1.getClickCount()) {
                    return 0;
                } else if (statisticsBean.getClickCount() > t1.getClickCount()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }
}

package com.chinapex.android.datacollect.model.bean.response;

import com.chinapex.android.datacollect.utils.GsonUtils;

import java.util.Map;

/**
 * @author SteelCabbage
 * @date 2019/01/08
 */
public class TestJson {
    public static final String JSON = "{\n" +
            "    \"code\": 200,\n" +
            "    \"data\": {\n" +
            "        \"config\": {\n" +
            "            \"click\": {\n" +
            "                \"32fa0ea9cecd07af1b56e85febe65329\": {\n" +
            "                    \"alias\": \"登出\",\n" +
            "                    \"definedPage\": \"com.chinapex.analytics.sample.activity.MainActivity\",\n" +
            "                    \"pageClassName\": \"com.chinapex.analytics.sample.activity.MainActivity\",\n" +
            "                    \"viewPath\": \"/##com.chinapex.analytics.sample.activity" +
            ".MainActivity##/RelativeLayout[0]/AppCompatButton[7]#bt_signOut\"\n" +
            "                }\n" +
            "            },\n" +
            "            \"list\": {\n" +
            "                \"019c49e84916d782155e2e085f5bbd3c\": {\n" +
            "                    \"definedPage\": \"com.chinapex.analytics.sample.activity.ClickTestActivity\",\n" +
            "                    \"itemAliases\": {\n" +
            "                        \"7cf8d54fb86060fc4917b22266163615\": \"list0\"\n" +
            "                    },\n" +
            "                    \"pageClassName\": \"com.chinapex.analytics.sample.activity.ClickTestActivity\",\n" +
            "                    \"viewPath\": \"/##com.chinapex.analytics.sample.activity" +
            ".ClickTestActivity##/RelativeLayout[0]/ListView[0]#list_view\"\n" +
            "                },\n" +
            "                \"31055aafb40e983e708d88d17ecf73e2\": {\n" +
            "                    \"definedPage\": \"com.chinapex.analytics.sample.activity.ClickTestActivity\",\n" +
            "                    \"itemAliases\": {\n" +
            "                        \"13bcf4836343d33384d52f2ed17d0942\": \"grid1\"\n" +
            "                    },\n" +
            "                    \"pageClassName\": \"com.chinapex.analytics.sample.activity.ClickTestActivity\",\n" +
            "                    \"viewPath\": \"/##com.chinapex.analytics.sample.activity" +
            ".ClickTestActivity##/RelativeLayout[0]/GridView[0]#grid_view\"\n" +
            "                },\n" +
            "                \"f039e8279f1ae5f51ba759ecd546be7b\": {\n" +
            "                    \"definedPage\": \"com.chinapex.analytics.sample.activity.FragmentActivity\",\n" +
            "                    \"itemAliases\": {\n" +
            "                        \"79361cb2479e5f2356783f586e5a21bd\": \"frag list0\"\n" +
            "                    },\n" +
            "                    \"pageClassName\": \"com.chinapex.analytics.sample.activity.FragmentActivity##com.chinapex" +
            ".analytics.sample.fragment.FragmentNoV4\",\n" +
            "                    \"viewPath\": \"/##com.chinapex.analytics.sample.activity" +
            ".FragmentActivity##/RelativeLayout[0]/FrameLayout[0]#second_fl/##com.chinapex.analytics.sample.fragment" +
            ".FragmentNoV4##/RelativeLayout[0]/ListView[0]#lv_frag\"\n" +
            "                }\n" +
            "            },\n" +
            "            \"pv\": {\n" +
            "                \"d35756815312bc5a6b010775aa187321\": {\n" +
            "                    \"alias\": \"列表页\",\n" +
            "                    \"viewPath\": \"com.chinapex.analytics.sample.activity.ClickTestActivity\"\n" +
            "                },\n" +
            "                \"b04ec75cdeaa52470e063d6734d14a95\": {\n" +
            "                    \"alias\": \"主页\",\n" +
            "                    \"viewPath\": \"com.chinapex.analytics.sample.activity.MainActivity\"\n" +
            "                },\n" +
            "                \"a00f729440118417d601e2c0a2763294\": {\n" +
            "                    \"alias\": \"frag页\",\n" +
            "                    \"viewPath\": \"com.chinapex.analytics.sample.activity.FragmentActivity\"\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        \"version\": \"1.0#1548658973237\"\n" +
            "    }\n" +
            "}";

    public static void main(String[] args) {

//        ArrayList<String> mViewPath = new ArrayList<>();
//        mViewPath.add("viewPath");
//        mViewPath.add("activityName");
//        mViewPath.add("fragment5");
//        mViewPath.add("fragment4");
//        mViewPath.add("fragment3");
//        mViewPath.add("fragment2");
//        mViewPath.add("fragment1");
//
//        String pageName = ConfigUtils.getPageClassName(mViewPath);
//        if (pageName == null || pageName.length() == 0) {
//            System.out.println("pageName is null or empty!");
//            return;
//        }
//
//        System.out.println("pageName:" + pageName);


        UpdateConfigResponse updateConfigResponse = GsonUtils.json2Bean(JSON, UpdateConfigResponse.class);
        if (null == updateConfigResponse) {
            System.out.println("updateConfigResponse is null!");
            return;
        }

        UpdateConfigResponse.DataBean data = updateConfigResponse.getData();
        if (null == data) {
            System.out.println("data is null!");
            return;
        }

        UpdateConfigResponse.DataBean.Config config = data.getConfig();
        if (null == config) {
            System.out.println("config is null!");
            return;
        }

        Map<String, UpdateConfigResponse.DataBean.Config.ListBean> list = config.getList();
        if (null == list || list.isEmpty()) {
            System.out.println("list is null or empty!");
            return;
        }

        for (Map.Entry<String, UpdateConfigResponse.DataBean.Config.ListBean> listBeanEntry : list.entrySet()) {
            if (null == listBeanEntry) {
                System.out.println("listBeanEntry is null!");
                continue;
            }

            String listId = listBeanEntry.getKey();
            System.out.println("listId:" + listId);
            UpdateConfigResponse.DataBean.Config.ListBean listBean = listBeanEntry.getValue();
            if (null == listBean) {
                System.out.println("listBean is null!");
                continue;
            }

            String definedPage = listBean.getDefinedPage();
            System.out.println("definedPage:" + definedPage);

            String pageClass = listBean.getPageClassName();
            System.out.println("pageClass:" + pageClass);

            String viewPath = listBean.getViewPath();
            System.out.println("viewPath:" + viewPath);

            Map<String, String> itemAliases = listBean.getItemAliases();
            if (null == itemAliases || itemAliases.isEmpty()) {
                System.out.println("itemAliases is null or empty!");
                continue;
            }

            for (Map.Entry<String, String> itemAlias : itemAliases.entrySet()) {
                if (null == itemAlias) {
                    System.out.println("itemAlias is null!");
                    continue;
                }

                String key = itemAlias.getKey();
                System.out.println("itemAlias key:" + key);

                String value = itemAlias.getValue();
                System.out.println("itemAlias value:" + value);
            }
        }
    }




}

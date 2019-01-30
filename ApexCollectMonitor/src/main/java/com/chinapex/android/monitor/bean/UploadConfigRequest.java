package com.chinapex.android.monitor.bean;

import java.util.Map;

/**
 * @author SteelCabbage
 * @date 2019/01/20
 */
public class UploadConfigRequest {

    private String version;
    private Config config;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public static class Config {

        private Map<String, ClickBean> click;
        private Map<String, PvBean> pv;
        private Map<String, ListBean> list;

        public Map<String, ClickBean> getClick() {
            return click;
        }

        public void setClick(Map<String, ClickBean> click) {
            this.click = click;
        }

        public Map<String, PvBean> getPv() {
            return pv;
        }

        public void setPv(Map<String, PvBean> pv) {
            this.pv = pv;
        }

        public Map<String, ListBean> getList() {
            return list;
        }

        public void setList(Map<String, ListBean> list) {
            this.list = list;
        }

        public static class ClickBean {
            /**
             * viewPath 唯一路径
             */
            private String viewPath;
            /**
             * 该view的源所属页面
             */
            private String pageClassName;
            /**
             * 该view被定义在哪个页面
             */
            private String definedPage;
            /**
             * 该事件的别名
             */
            private String alias;
            /**
             * 大的更新时, 与之前的同一事件进行关联
             */
            private String preMD5;

            public String getViewPath() {
                return viewPath;
            }

            public void setViewPath(String viewPath) {
                this.viewPath = viewPath;
            }

            public String getPageClassName() {
                return pageClassName;
            }

            public void setPageClassName(String pageClassName) {
                this.pageClassName = pageClassName;
            }

            public String getDefinedPage() {
                return definedPage;
            }

            public void setDefinedPage(String definedPage) {
                this.definedPage = definedPage;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getPreMD5() {
                return preMD5;
            }

            public void setPreMD5(String preMD5) {
                this.preMD5 = preMD5;
            }
        }

        public static class PvBean {
            /**
             * 页面的唯一路径
             */
            private String viewPath;
            /**
             * 该事件的别名
             */
            private String alias;
            /**
             * 大的更新时, 与之前的同一事件进行关联
             */
            private String preMD5;

            public String getViewPath() {
                return viewPath;
            }

            public void setViewPath(String viewPath) {
                this.viewPath = viewPath;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getPreMD5() {
                return preMD5;
            }

            public void setPreMD5(String preMD5) {
                this.preMD5 = preMD5;
            }
        }

        public static class ListBean {
            /**
             * 列表的ID
             */
            private String viewPath;
            /**
             * 该列表的源所属页面
             */
            private String pageClassName;
            /**
             * 该列表被定义在哪个页面
             */
            private String definedPage;
            /**
             * 列表项的别名集合, key: MD5(列表id + dataKey), value: alias
             */
            private Map<String, String> itemAliases;

            public String getViewPath() {
                return viewPath;
            }

            public void setViewPath(String viewPath) {
                this.viewPath = viewPath;
            }

            public String getPageClassName() {
                return pageClassName;
            }

            public void setPageClassName(String pageClassName) {
                this.pageClassName = pageClassName;
            }

            public String getDefinedPage() {
                return definedPage;
            }

            public void setDefinedPage(String definedPage) {
                this.definedPage = definedPage;
            }

            public Map<String, String> getItemAliases() {
                return itemAliases;
            }

            public void setItemAliases(Map<String, String> itemAliases) {
                this.itemAliases = itemAliases;
            }
        }
    }
}

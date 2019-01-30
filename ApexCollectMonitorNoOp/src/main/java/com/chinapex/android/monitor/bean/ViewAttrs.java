package com.chinapex.android.monitor.bean;

/**
 * @author SteelCabbage
 * @date 2018/12/14
 */
public class ViewAttrs {
    private String pageClassName;
    private String viewPath;
    private String viewPathMD5;
    /**
     * 有可能是view的截图
     */
    private String content;

    public String getPageClassName() {
        return pageClassName;
    }

    public void setPageClassName(String pageClassName) {
        this.pageClassName = pageClassName;
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getViewPathMD5() {
        return viewPathMD5;
    }

    public void setViewPathMD5(String viewPathMD5) {
        this.viewPathMD5 = viewPathMD5;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

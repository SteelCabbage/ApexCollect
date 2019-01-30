package com.chinapex.analytics.sample.bean;

/**
 * @author SteelCabbage
 * @date 2018/11/16
 */
public class Student {
    private String name;
    private int age;
    private Info mInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Info getInfo() {
        return mInfo;
    }

    public void setInfo(Info info) {
        mInfo = info;
    }

    public static class Info {
        private String nameInner;

        public String getNameInner() {
            return nameInner;
        }

        public void setNameInner(String nameInner) {
            this.nameInner = nameInner;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "nameInner='" + nameInner + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", mInfo=" + mInfo +
                '}';
    }
}

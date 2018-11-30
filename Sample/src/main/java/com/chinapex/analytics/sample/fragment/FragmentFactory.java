package com.chinapex.analytics.sample.fragment;


import android.app.Fragment;

/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class FragmentFactory {

    private static FragmentV4 sFragmentV4;
    private static FragmentNoV4 sFragmentNoV4;
    private static ViewPagerFragment1 sViewPagerFragment1;
    private static ViewPagerFragment2 sViewPagerFragment2;

    public static BaseFragmentV4 getFragment(String fragmentTag) {
        BaseFragmentV4 baseFragmentV4 = null;
        switch (fragmentTag) {
            case "FragmentV4":
                if (null == sFragmentV4) {
                    sFragmentV4 = new FragmentV4();
                }
                baseFragmentV4 = sFragmentV4;
                break;
            case "ViewPagerFragment1":
                if (null == sViewPagerFragment1) {
                    sViewPagerFragment1 = new ViewPagerFragment1();
                }
                baseFragmentV4 = sViewPagerFragment1;
                break;
            case "ViewPagerFragment2":
                if (null == sViewPagerFragment2) {
                    sViewPagerFragment2 = new ViewPagerFragment2();
                }
                baseFragmentV4 = sViewPagerFragment2;
                break;
            default:
                break;
        }
        return baseFragmentV4;
    }

    public static Fragment getFragmentNoV4(String fragmentTag) {
        Fragment fragment = null;
        switch (fragmentTag) {
            case "FragmentNoV4":
                if (null == sFragmentNoV4) {
                    sFragmentNoV4 = new FragmentNoV4();
                }
                fragment = sFragmentNoV4;
                break;
            default:
                break;
        }
        return fragment;
    }
}

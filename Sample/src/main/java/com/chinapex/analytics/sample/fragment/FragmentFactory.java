package com.chinapex.analytics.sample.fragment;


import android.app.Fragment;

/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class FragmentFactory {

    private static FragmentV4 sFragmentV4;
    private static FragmentNoV4 sFragmentNoV4;

    public static BaseFragmentV4 getFragment(String fragmentTag) {
        BaseFragmentV4 baseFragmentV4 = null;
        switch (fragmentTag) {
            case "FragmentV4":
                if (null == sFragmentV4) {
                    sFragmentV4 = new FragmentV4();
                }
                baseFragmentV4 = sFragmentV4;
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

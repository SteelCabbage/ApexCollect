package com.chinapex.analytics.sample.fragment;



/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class FragmentFactory {

    private static FragmentV4 sFragmentV4;

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
}

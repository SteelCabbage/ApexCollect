package com.chinapex.android.datacollect.controller;

/**
 * @author SteelCabbage
 * @date 2018/11/23
 */
public class LocationController implements IController {

    private static final String TAG = LocationController.class.getSimpleName();

    private LocationController() {

    }

    private static class LocationControllerHolder {
        private static final LocationController LOCATION_CONTROLLER = new LocationController();
    }

    public static LocationController getInstance() {
        return LocationControllerHolder.LOCATION_CONTROLLER;
    }

    @Override
    public void doInit() {

    }

    @Override
    public void onDestroy() {

    }


}

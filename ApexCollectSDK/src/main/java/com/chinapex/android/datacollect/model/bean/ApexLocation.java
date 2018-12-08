package com.chinapex.android.datacollect.model.bean;

/**
 * @author SteelCabbage
 * @date 2018/12/06
 */
public class ApexLocation {
    private final double longitude;
    private final double latitude;
    private final String country;
    private final String province;
    private final String city;
    private final String district;


    private ApexLocation(LocationBuilder locationBuilder) {
        this.longitude = locationBuilder.longitude;
        this.latitude = locationBuilder.latitude;
        this.country = locationBuilder.country;
        this.province = locationBuilder.province;
        this.city = locationBuilder.city;
        this.district = locationBuilder.district;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public static class LocationBuilder {
        private double longitude;
        private double latitude;
        private String country;
        private String province;
        private String city;
        private String district;

        public LocationBuilder setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public LocationBuilder setLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public LocationBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public LocationBuilder setProvince(String province) {
            this.province = province;
            return this;
        }

        public LocationBuilder setCity(String city) {
            this.city = city;
            return this;
        }

        public LocationBuilder setDistrict(String district) {
            this.district = district;
            return this;
        }

        public ApexLocation build() {
            return new ApexLocation(this);
        }
    }

    @Override
    public String toString() {
        return "ApexLocation{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}

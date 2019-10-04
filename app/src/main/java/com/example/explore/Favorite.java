package com.example.explore;

public class Favorite {
   private String placeid;
   private int id;
   private int userid;
   private String Address;
   private String PlaceName;




    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getAddress() {
        return Address;
    }
    public  String getPlaceName(){
        return PlaceName;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }
}

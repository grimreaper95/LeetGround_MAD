package edu.neu.madcourse.leetground;

public class ShopItem {
    String title;
    String price;
    String imageurl;

    public ShopItem(String mtitle,String mprice,String mimageurl){
        title = mtitle;
        price = mprice;
        imageurl = mimageurl;
    }

    public String getTitle(){
        return title;
    }
    public String getPrice(){
        return price;
    }


    public String getImageurl(){
        return imageurl;
    }

}

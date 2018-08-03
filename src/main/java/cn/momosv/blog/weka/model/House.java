package cn.momosv.blog.weka.model;

import cn.momosv.blog.weka.annotation.MyInstances;

@MyInstances(RELATION = "house", CLASS_INDEX =0,INDEX = -1)//-1不需要的意思
public class House {

    public House() {
    }

    public House(double sellingPrice, double houseSize, double lotSize, int bedroom, int granite, double bathroom) {
        this.sellingPrice = sellingPrice;
        this.houseSize = houseSize;
        this.lotSize = lotSize;
        this.bedroom = bedroom;
        this.granite = granite;
        this.bathroom = bathroom;
    }

    @MyInstances(ATTRIBUTE = "sellingPrice", INDEX = 0)
    private double sellingPrice;
    @MyInstances(ATTRIBUTE = "houseSize", INDEX = 1)
    private double houseSize;
    @MyInstances(ATTRIBUTE = "lotSize", INDEX = 2)
    private double lotSize;
    @MyInstances(ATTRIBUTE = "bedroom", INDEX = 3)
    private int bedroom;
    @MyInstances(ATTRIBUTE = "granite", INDEX = 4,value = {"0","1"})
    private int granite;
    @MyInstances(ATTRIBUTE = "bathroom", INDEX = 5)
    private double bathroom;




    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getHouseSize() {
        return houseSize;
    }

    public void setHouseSize(double houseSize) {
        this.houseSize = houseSize;
    }

    public double getLotSize() {
        return lotSize;
    }

    public void setLotSize(double lotSize) {
        this.lotSize = lotSize;
    }

    public int getBedroom() {
        return bedroom;
    }

    public void setBedroom(int bedroom) {
        this.bedroom = bedroom;
    }

    public int getGranite() {
        return granite;
    }

    public void setGranite(int granite) {
        this.granite = granite;
    }

    public double getBathroom() {
        return bathroom;
    }

    public void setBathroom(double bathroom) {
        this.bathroom = bathroom;
    }
}

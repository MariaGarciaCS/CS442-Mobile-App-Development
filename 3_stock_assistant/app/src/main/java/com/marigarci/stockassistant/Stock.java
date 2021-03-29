package com.marigarci.stockassistant;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Stock implements Comparable<Stock> {
    String symbol; //stock symbol
    String company; //companyName
    double price;
    double pChange; //price change
    double percent; //change percent

    private static DecimalFormat df2 = new DecimalFormat("0.00");

    //Constructors
    public Stock(){
        this.symbol = null;
        this.company = null;
        this.price = 0.0;
        this.pChange = 0.0;
        this.percent = 0.0;
    }
    public Stock(String symbol, String company, double price, double priceChange, double percentChange){
        this.symbol = symbol;
        this.company = company;
        this.price = price;
        this.pChange = priceChange;
        this.percent = percentChange;
    }
    public Stock(String symbol, String company){
        this.symbol = symbol;
        this.company = company;
    }

    //get methods
    public String getSymbol() {
        return symbol;
    }
    public String getCompany() {
        return company;
    }
    public double getPrice() {
        return price;
    }
    public String getPriceStr(){return df2.format(price);}
    public double getpChange() {
        return pChange;
    }
    public String getpChangeStr() {
        return df2.format(pChange);
    }
    public double getPercent() {
        return percent;
    }
    public String getPercentStr() {
        return df2.format((percent));
    }

    //set methods
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setpChange(double pChange) {
        this.pChange = pChange;
    }
    public void setPercent(double percent) {
        this.percent = percent;
    }
    public void setAll(String symbol, String company, double price, double priceChange, double percentChange){
        this.symbol = symbol;
        this.company = company;
        this.price = price;
        this.pChange = priceChange;
        this.percent = percentChange;
    }

    @Override
    public int compareTo(Stock s){
        return this.getSymbol().compareTo( s.getSymbol() );
    }
}

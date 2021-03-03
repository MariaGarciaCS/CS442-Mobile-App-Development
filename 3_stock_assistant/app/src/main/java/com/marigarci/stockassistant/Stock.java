package com.marigarci.stockassistant;

public class Stock {
    private String symbol; //stock symbol
    private String company; //companyName
    private double price;
    private double pChange; //price change
    private double percent; //change percent

    //Constructors
    public Stock(String symbol, String company, double price, double priceChange, double percentChange){
        this.symbol = symbol;
        this.company = company;
        this.price = price;
        this.pChange = priceChange;
        this.percent = percentChange;
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
    public double getpChange() {
        return pChange;
    }
    public double getPercent() {
        return percent;
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
}

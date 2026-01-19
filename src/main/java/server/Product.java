package server;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Product {
    private int id;
    private String name;
    private int price;
    private int qty;

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getQty() { return qty; }
}

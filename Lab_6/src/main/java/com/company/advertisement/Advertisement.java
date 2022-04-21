package com.company.advertisement;
import com.company.billboards.Order;


public class Advertisement {
    public Order order;
    public int billboardId;
    public int orderId;

    public Advertisement(int billboardId, int orderId,Order order){
        this.billboardId = billboardId;
        this.orderId = orderId;
        this.order = order;
    }

}

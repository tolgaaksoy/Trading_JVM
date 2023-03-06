package main.java.model;

import java.util.Objects;

// The Order class represents an order to buy or sell a certain quantity of a stock at a specific price.

public class Order implements Comparable<Order> {
    // The id of the order, used to uniquely identify it.
    private final String id;

    // The type of the order, either OrderType.BID (for buy) or OrderType.ASK (for sell).
    private final OrderType orderType;

    // The price at which the order is placed.
    private final int price;

    // The quantity of the stock that is being bought or sold.
    private final int quantity;

    // Constructs a new Order object with the given id, order type, price, and quantity.
    public Order(String id, OrderType orderType, int price, int quantity) {

        // Initialize the instance variables with the given parameters.
        this.id = id;
        this.orderType = orderType;
        this.price = price;
        this.quantity = quantity;
    }

    // Returns the id of the order.
    public String getId() {
        return id;
    }

    // Returns the price of the order.
    public int getPrice() {
        return price;
    }

    // Returns the type of the order.
    public OrderType getOrderType() {
        return orderType;
    }

    // Returns the quantity of the stock that is being bought or sold.
    public int getQuantity() {
        return quantity;
    }

    // Returns a string representation of the order, including its id, type, price, and quantity.
    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", orderType=" + orderType +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    // Returns true if this order is equal to the given object.
    // Two orders are considered equal if they have the same id, type, price, and quantity.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return price == order.price && quantity == order.quantity && id.equals(order.id) && orderType == order.orderType;
    }

    // Returns the hash code of the order, based on its id, type, price, and quantity.
    @Override
    public int hashCode() {
        return Objects.hash(id, orderType, price, quantity);
    }

    // Compares this order to the given order, based on their type, price, and id.
    // Orders with the same type are sorted by their price, with buy orders (bids) sorted in descending order,
    // and sell orders (asks) sorted in ascending order.
    // If two orders have the same price, they are sorted by their id in ascending order.
    @Override
    public int compareTo(Order o) {
        if (!this.getOrderType().equals(o.getOrderType())) {
            throw new IllegalArgumentException("Cannot compare orders with different sides");
        }
        int priceComparison = Integer.compare(o.getPrice(), this.getPrice());
        if (priceComparison != 0) {
            return this.getOrderType().equals(OrderType.BID) ? priceComparison : -priceComparison;
        } else {
            return this.getId().compareTo(o.getId());
        }
    }
}
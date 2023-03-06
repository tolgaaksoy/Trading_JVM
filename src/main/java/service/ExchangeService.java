package main.java.service;

import main.java.model.Order;
import main.java.model.OrderBook;
import main.java.model.OrderType;

public class ExchangeService {

    private final OrderBook orderBook;

    public ExchangeService() {
        this.orderBook = new OrderBook();
    }


    // The match method has a constant time complexity of O(1) because it simply performs a conditional and then calls either matchAgainstSellOrders or matchAgainstBuyOrders.

    /**
     * Match incoming order with the order book and produce trades
     *
     * @param order The order to match against the order book.
     */
    public void match(Order order) {
        if (order.getOrderType().equals(OrderType.BID)) {
            matchAgainstSellOrders(order);
        } else {
            matchAgainstBuyOrders(order);
        }
    }

    // The matchAgainstSellOrders method has a time complexity that depends on the size of the asks SortedSet in the orderBook field,
    // which can be O(n) in the worst case (when order has a high price and there are many sell orders with prices lower than order.getPrice()),
    // but is typically much lower. In addition to iterating over the asks set, the method also calls several O(1) methods (fetchBestAsk, addTrade, addOrder, etc.)
    // and performs a few constant-time operations like comparisons and arithmetic.

    /**
     * Matches the given order against existing sell orders in the order book.
     *
     * @param order The order to match against the sell orders.
     */
    private void matchAgainstSellOrders(Order order) {

        // Check if there are any sell orders (this.orderBook.getAsks()) and if the best ask price is lower than or equal to the order price and if the order quantity is greater than zero
        while (!this.orderBook.isAsksEmpty() &&
                this.orderBook.getBestAsk().getPrice() <= order.getPrice() &&
                order.getQuantity() > 0) {

            // Get the best ask order
            Order ask = this.orderBook.fetchBestAsk();

            // Calculate the trade quantity as the minimum of the order and ask quantities
            int tradeQuantity = Math.min(order.getQuantity(), ask.getQuantity());

            // Add the trade to the order book
            orderBook.addTrade(order, ask, tradeQuantity);

            // Update the order quantity after the trade
            order = new Order(order.getId(), order.getOrderType(), order.getPrice(), order.getQuantity() - tradeQuantity);

            // Update the ask quantity after the trade
            ask = new Order(ask.getId(), ask.getOrderType(), ask.getPrice(), ask.getQuantity() - tradeQuantity);

            // If the ask still has quantity, add it back to the ask orders
            if (ask.getQuantity() > 0) {
                this.orderBook.addOrder(ask);
            }
        }

        // If the order still has quantity, add it to the bid orders
        if (order.getQuantity() > 0) {
            this.orderBook.addOrder(order);
        }
    }

    // The matchAgainstBuyOrders method has a time complexity that depends on the size of the bids SortedSet in the orderBook field,
    // which can be O(n) in the worst case (when order has a low price and there are many buy orders with prices higher than order.getPrice()),
    // but is typically much lower. In addition to iterating over the bids set, the method also calls several O(1) methods (fetchBestBid, addTrade, addOrder, etc.)
    // and performs a few constant-time operations like comparisons and arithmetic.

    /**
     * Matches the given order against existing buy orders in the order book.
     *
     * @param order The order to match against the buy orders.
     */
    private void matchAgainstBuyOrders(Order order) {

        // Check if there are any buy orders (this.orderBook.getBids()) and if the best bid price is higher than or equal to the order price and if the order quantity is greater than zero
        while (!this.orderBook.isBidsEmpty() &&
                this.orderBook.getBestBid().getPrice() >= order.getPrice() &&
                order.getQuantity() > 0) {

            // Get the best bid order
            Order bid = this.orderBook.fetchBestBid();

            // Calculate the trade quantity as the minimum of the order and bid quantities
            int tradeQuantity = Math.min(order.getQuantity(), bid.getQuantity());

            // Add the trade to the order book
            orderBook.addTrade(bid, order, tradeQuantity);

            // Update the order quantity after the trade
            order = new Order(order.getId(), order.getOrderType(), order.getPrice(), order.getQuantity() - tradeQuantity);

            // Update the bid quantity after the trade
            bid = new Order(bid.getId(), bid.getOrderType(), bid.getPrice(), bid.getQuantity() - tradeQuantity);

            // If the bid still has quantity, add it back to the bid orders
            if (bid.getQuantity() > 0) {
                this.orderBook.addOrder(bid);
            }
        }

        // If the order still has quantity, add it to the ask orders
        if (order.getQuantity() > 0) {
            this.orderBook.addOrder(order);
        }
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }
}
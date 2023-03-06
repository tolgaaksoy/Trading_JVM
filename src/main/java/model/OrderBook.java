package main.java.model;

import java.util.*;

public class OrderBook {
    private final List<Trade> trades = new LinkedList<>(); //A list to store all trades made
    // Buy orders
    private final SortedSet<Order> bids = new TreeSet<>(); //A SortedSet to store Buy Orders, sorted by price (the highest first)
    // Sell orders
    private final SortedSet<Order> asks = new TreeSet<>(); //A SortedSet to store Sell Orders, sorted by price (the lowest first)

    //This method adds an Order to the OrderBook
    public void addOrder(Order order) {
        if (order.getOrderType().equals(OrderType.BID)) { //If the order is a Buy Order
            bids.add(order); //Add the order to the bids SortedSet
        } else { //If the order is a Sell Order
            asks.add(order); //Add the order to the asks SortedSet
        }
    }

    //This method checks if there are any Buy Orders in the Order Book
    public boolean isBidsEmpty() {
        return this.bids.isEmpty(); //Returns true if the bids SortedSet is empty, false otherwise
    }

    //This method checks if there are any Sell Orders in the Order Book
    public boolean isAsksEmpty() {
        return this.asks.isEmpty(); //Returns true if the asks SortedSet is empty, false otherwise
    }

    //This method returns the best Buy Order (the one with the highest bid price) in the Order Book
    public Order getBestBid() {
        //If there are no Buy Orders in the Order Book
        if (this.bids.isEmpty()) {
            return null; //Return null (no best Buy Order
        }
        return this.bids.first(); //Returns the first (highest) element in the bids SortedSet
    }

    //This method returns the best Sell Order (the one with the lowest ask price) in the Order Book
    public Order getBestAsk() {
        //If there are no Sell Orders in the Order Book
        if (this.asks.isEmpty()) {
            return null; //Return null (no best Sell Order
        }
        return this.asks.first(); //Returns the first (lowest) element in the asks SortedSet
    }

    //This method fetches and removes the best Buy Order from the Order Book
    public Order fetchBestBid() {
        //If there are no Buy Orders in the Order Book
        if (this.bids.isEmpty()) {
            return null; //Return null (no best Buy Order
        }
        Order bestBid = bids.first(); //Gets the best Buy Order
        this.bids.remove(bestBid); //Removes the best Buy Order from the bids SortedSet
        return bestBid; //Returns the best Buy Order
    }

    //This method fetches and removes the best Sell Order from the Order Book
    public Order fetchBestAsk() {
        //If there are no Sell Orders in the Order Book
        if (this.asks.isEmpty()) {
            return null; //Return null (no best Sell Order
        }
        Order bestAsk = asks.first(); //Gets the best Sell Order
        this.asks.remove(bestAsk); //Removes the best Sell Order from the asks SortedSet
        return bestAsk; //Returns the best Sell Order
    }

    //This method adds a Trade to the Order Book
    public void addTrade(Order bid, Order ask, int tradeQuantity) {
        this.trades.add(new Trade(bid, ask, tradeQuantity, ask.getPrice())); //Creates a new Trade object and adds it to the trade list
    }

    // Get a list of strings representing the current order book
    public List<String> getOrderBook() {
        LinkedList<String> orderBook = new LinkedList<>();

        // Create iterators for the bids and asks queues
        Iterator<Order> bidIterator = bids.iterator();
        Iterator<Order> askIterator = asks.iterator();

        // Iterate over the bids and asks and format them into strings
        while (bidIterator.hasNext() || askIterator.hasNext()) {
            String buyLine = String.format("%-18s", "");
            String sellLine = String.format("%-18s", "");

            if (bidIterator.hasNext()) {
                Order bidOrder = bidIterator.next();
                // Format the bid and ask prices and quantities
                String bidPrice = String.format("%d", bidOrder.getPrice());
                String bidQuantity = String.format("%,d", bidOrder.getQuantity());
                buyLine = String.format("%11s%7s", bidQuantity, bidPrice);

            }
            if (askIterator.hasNext()) {
                Order askOrder = askIterator.next();
                // Format the bid and ask prices and quantities
                String askPrice = String.format("%d", askOrder.getPrice());
                String askQuantity = String.format("%,d", askOrder.getQuantity());
                sellLine = String.format("%6s%12s", askPrice, askQuantity);
            }
            orderBook.add(buyLine + " | " + sellLine);
        }
        return orderBook;
    }

    // Get a list of strings representing the trades that have occurred
    public List<String> getTrades() {
        LinkedList<String> tradeList = new LinkedList<>();
        for (Trade trade : this.trades) {
            tradeList.add(String.format("trade %s,%s,%d,%d", trade.bid.getId(), trade.ask.getId(), trade.tradePrice, trade.tradeQuantity));
        }
        return Collections.unmodifiableList(tradeList);
    }

    public void writeTradeAndOrderBook() {
        // Print messages for all the trades that have occurred
        for (Trade trade : this.trades) {
            System.out.printf("Trading %d units at %d between %s(%s) and %s(%s).%n", trade.tradeQuantity, trade.tradePrice, trade.bid.getId(), trade.bid.getOrderType(), trade.ask.getId(), trade.ask.getOrderType());
        }
        // Find the highest bid and lowest ask prices
        int maxBidPrice = bids.isEmpty() ? 0 : bids.stream().max(Comparator.comparing(Order::getPrice)).get().getPrice();
        int minAskPrice = asks.isEmpty() ? 0 : asks.stream().min(Comparator.comparing(Order::getPrice)).get().getPrice();

        // Print the header
        System.out.printf("%18s%s | %-7s%11s%n", "BIDS", "", "ASKS", "");
        System.out.printf("%11s%7s | %-7s%11s%n", "Quantity", "Price", "Price", "Quantity");

        // Print the highest bid and lowest ask prices
        for (String line : this.getOrderBook()) {
            System.out.println(line);
        }
        // Print the highest bid and lowest ask prices
        System.out.printf("%nHighest Bid: %,d\tLowest Ask: %,d%n", maxBidPrice, minAskPrice);
    }

    public SortedSet<Order> getBids() {
        return bids;
    }

    public SortedSet<Order> getAsks() {
        return asks;
    }

    public static class Trade {
        private final Order bid;
        private final Order ask;
        private final int tradeQuantity;
        private final int tradePrice;

        public Trade(Order bid, Order ask, int tradeQuantity, int tradePrice) {
            this.bid = bid;
            this.ask = ask;
            this.tradeQuantity = tradeQuantity;
            this.tradePrice = tradePrice;
        }
    }
}
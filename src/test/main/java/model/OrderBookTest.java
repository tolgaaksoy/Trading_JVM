package test.main.java.model;

import main.java.model.Order;
import main.java.model.OrderBook;
import main.java.model.OrderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderBookTest {

    private OrderBook orderBook;

    @BeforeEach
    void setUp() {
        this.orderBook = new OrderBook();
    }

    @Test
    public void addOrder_AddingOrdersToOrderBook_BestBidAndBestAskAreUpdated() {

        // given
        Order bid = new Order("10000", OrderType.BID, 100, 10);
        Order ask = new Order("10001", OrderType.ASK, 110, 5);

        // when
        orderBook.addOrder(bid);
        orderBook.addOrder(ask);

        // then
        assertEquals(bid, orderBook.getBestBid());
        assertEquals(ask, orderBook.getBestAsk());
    }

    @Test
    public void isBidsEmpty_BidsAdded_ReturnsFalse() {

        // given
        Order bid = new Order("10000", OrderType.BID, 100, 10);

        // when
        orderBook.addOrder(bid);

        // then
        assertFalse(orderBook.isBidsEmpty());
    }

    @Test
    public void isBidsEmpty_NoBidsAdded_ReturnsTrue() {
        assertTrue(orderBook.isBidsEmpty());
    }

    @Test
    public void isAsksEmpty_NoAsksAdded_ReturnsTrue() {
        assertTrue(orderBook.isAsksEmpty());
    }

    @Test
    public void isAsksEmpty_AsksAdded_ReturnsFalse() {

        // given
        Order ask = new Order("10000", OrderType.ASK, 110, 5);

        // when
        orderBook.addOrder(ask);

        // then
        assertFalse(orderBook.isAsksEmpty());
    }

    @Test
    public void getBestBid_NoBidsAdded_ReturnsNull() {
        assertNull(orderBook.getBestBid());
    }

    @Test
    public void getBestAsk_NoAsksAdded_ReturnsNull() {
        assertNull(orderBook.getBestAsk());
    }

    @Test
    public void getBestBid_BidsAdded_ReturnsBestBid() {

        // given
        Order bid1 = new Order("10000", OrderType.BID, 100, 10);
        Order bid2 = new Order("10002", OrderType.BID, 110, 5);

        // when
        orderBook.addOrder(bid1);
        orderBook.addOrder(bid2);

        // then
        assertEquals(bid2, orderBook.getBestBid());
    }

    @Test
    public void getBestAsk_AsksAdded_ReturnsBestAsk() {
        // given
        Order ask1 = new Order("10000", OrderType.ASK, 110, 5);
        Order ask2 = new Order("10002", OrderType.ASK, 100, 10);

        // when
        orderBook.addOrder(ask1);
        orderBook.addOrder(ask2);

        // then
        assertEquals(ask2, orderBook.getBestAsk());
    }

}

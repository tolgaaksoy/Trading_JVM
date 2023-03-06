package test.main.java.service;

import main.java.model.Order;
import main.java.model.OrderBook;
import main.java.model.OrderType;
import main.java.service.ExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeServiceTest {

    private ExchangeService exchangeService;

    @BeforeEach
    void setUp() {
        exchangeService = new ExchangeService();
    }

    @Test
    void matchAgainstSellOrders_MatchingOrderExists_TradesProduced() {
        // given
        Order sellOrder = new Order("10000", OrderType.ASK, 100, 10);
        Order buyOrder = new Order("10001", OrderType.BID, 100, 10);

        exchangeService.getOrderBook().addOrder(sellOrder);

        // when
        exchangeService.match(buyOrder);

        // then
        assertFalse(exchangeService.getOrderBook().getTrades().isEmpty());
        assertTrue(exchangeService.getOrderBook().isAsksEmpty());
        assertTrue(exchangeService.getOrderBook().isBidsEmpty());
    }

    @Test
    void matchAgainstSellOrders_NoMatchingOrderExists_NoTradesProduced() {
        // given
        Order sellOrder = new Order("10000", OrderType.ASK, 100, 10);
        Order buyOrder = new Order("10001", OrderType.BID, 90, 10);

        exchangeService.getOrderBook().addOrder(sellOrder);

        // when
        exchangeService.match(buyOrder);

        // then
        assertEquals(0, exchangeService.getOrderBook().getTrades().size());
        assertFalse(exchangeService.getOrderBook().isAsksEmpty());
        assertFalse(exchangeService.getOrderBook().isBidsEmpty());
    }

    @Test
    void matchAgainstBuyOrders_NoMatchingOrderExists_NoTradesProduced() {
        // given
        Order buyOrder = new Order("10000", OrderType.BID, 90, 10);
        Order sellOrder = new Order("10001", OrderType.ASK, 100, 10);

        exchangeService.getOrderBook().addOrder(buyOrder);

        // when
        exchangeService.match(sellOrder);

        // then
        assertEquals(0, exchangeService.getOrderBook().getTrades().size());
        assertFalse(exchangeService.getOrderBook().isAsksEmpty());
        assertFalse(exchangeService.getOrderBook().isBidsEmpty());
    }

    @Test
    public void matchAgainstSellOrders_NoSellOrderExists_OrderAddedToAsks() {
        Order order = new Order("10000", OrderType.ASK, 100, 10);
        this.exchangeService.match(order);
        assertFalse(this.exchangeService.getOrderBook().isAsksEmpty());
        assertEquals(0, exchangeService.getOrderBook().getTrades().size());
    }


    @Test
    void getOrderBook_EmptyOrderBook_ReturnsEmptyOrderBook() {
        // given

        // when
        OrderBook orderBook = exchangeService.getOrderBook();

        // then
        assertNotNull(orderBook);
        assertTrue(orderBook.getBids().isEmpty());
        assertTrue(orderBook.getAsks().isEmpty());
        assertTrue(orderBook.getTrades().isEmpty());
    }

    @Test
    void getOrderBook_NonEmptyOrderBook_ReturnsNonEmptyOrderBook() {
        // given
        Order buyOrder = new Order("10000", OrderType.BID, 100, 10);
        Order sellOrder = new Order("10001", OrderType.ASK, 90, 10);

        exchangeService.getOrderBook().addOrder(buyOrder);
        exchangeService.getOrderBook().addOrder(sellOrder);

        // when
        OrderBook orderBook = exchangeService.getOrderBook();

        // then
        assertNotNull(orderBook);
        assertFalse(orderBook.getBids().isEmpty());
        assertFalse(orderBook.getAsks().isEmpty());
        assertTrue(orderBook.getTrades().isEmpty());
    }

}
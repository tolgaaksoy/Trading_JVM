package test.main.java.model;

import main.java.model.Order;
import main.java.model.OrderType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    void orderConstructor_IdTypePriceQuantity_OrderAttributesSet() {
        Order order = new Order("10000", OrderType.BID, 100, 10);
        assertEquals("10000", order.getId());
        assertEquals(OrderType.BID, order.getOrderType());
        assertEquals(100, order.getPrice());
        assertEquals(10, order.getQuantity());
    }

    @Test
    void orderEquals_IdTypePriceQuantity_SameOrdersEqual() {
        Order order1 = new Order("10000", OrderType.BID, 100, 10);
        Order order2 = new Order("10000", OrderType.BID, 100, 10);
        Order order3 = new Order("10001", OrderType.ASK, 200, 20);
        assertEquals(order1, order2);
        assertNotEquals(order1, order3);
    }

    @Test
    void orderHashCode_IdTypePriceQuantity_SameOrdersHaveSameHashCode() {
        Order order1 = new Order("10000", OrderType.BID, 100, 10);
        Order order2 = new Order("10000", OrderType.BID, 100, 10);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    void orderCompareTo_IdTypePriceQuantity_OrdersSortedAccordingToPrice() {
        Order order1 = new Order("10000", OrderType.BID, 100, 10);
        Order order2 = new Order("10001", OrderType.BID, 150, 10);
        Order order3 = new Order("10002", OrderType.ASK, 200, 10);
        assertEquals(1, order1.compareTo(order2));
        assertEquals(-1, order2.compareTo(order1));
        assertThrows(IllegalArgumentException.class, () -> order1.compareTo(order3));
    }

}

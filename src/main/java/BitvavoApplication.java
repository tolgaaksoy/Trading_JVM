package main.java;

import main.java.model.Order;
import main.java.service.ExchangeService;
import main.java.service.util.OrderReader;
import main.java.service.util.OrderWriter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BitvavoApplication {

    // The main method
    public static void main(String[] args) {

        // Map to keep track of processed files
        Map<String, String> processedFileMap = new HashMap<>();

        // Instantiate the OrderReader and OrderWriter classes
        OrderReader orderReader = new OrderReader();
        OrderWriter orderWriter = new OrderWriter();

        // Loop continuously to listen to new files
        while (true) {

            // Get the list of new files that have not been processed yet
            Map<String, LinkedList<Order>> fileOrderMap = orderReader.readFiles(processedFileMap.keySet());

            // If there are new files, process them and add them to the processed file map
            if (!fileOrderMap.isEmpty()) {
                processedFileMap.putAll(process(fileOrderMap, orderWriter));
            }

            // Sleep for 1 second
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * Process the files in the fileOrderMap
     *
     * @param fileOrderMap The map containing the files and their orders
     * @param orderWriter  The OrderWriter class
     * @return The processed file map
     */
    private static Map<String, String> process(Map<String, LinkedList<Order>> fileOrderMap, OrderWriter orderWriter) {

        // Map to keep track of processed files
        Map<String, String> processedFileMap = new HashMap<>();

        // Loop through each file in the fileOrderMap
        for (Map.Entry<String, LinkedList<Order>> fileOrder : fileOrderMap.entrySet()) {
            System.out.println("--------------------------------------------------------------------------------");

            System.out.println("Processing new file: " + fileOrder.getKey());

            // Instantiate the ExchangeService class
            ExchangeService exchange = new ExchangeService();

            // Loop through each order in the file and match them
            for (Order order : fileOrder.getValue()) {
                exchange.match(order);
            }

            // Print the trade and order book
            System.out.println("------------------------------------------------------------");
            exchange.getOrderBook().writeTradeAndOrderBook();
            System.out.println("------------------------------------------------------------");

            // Write the trade and order book to file
            orderWriter.writeFile(fileOrder.getKey(), exchange.getOrderBook().getTrades(), exchange.getOrderBook().getOrderBook());

            // Calculate and print the checksum
            String checksum = orderWriter.checksum(fileOrder.getKey());
            System.out.println("Checksum(MD5): " + checksum);

            // Add the file to the processed file map with its checksum
            processedFileMap.put(fileOrder.getKey(), checksum);

            System.out.println("--------------------------------------------------------------------------------");
        }

        return processedFileMap;
    }
}
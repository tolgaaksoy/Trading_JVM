package main.java.service.util;

import main.java.model.Order;
import main.java.model.OrderType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// This is the class declaration for the OrderReader class.
public class OrderReader {
    // This is a private field of the OrderReader class that represents the directory to read files from.
    private final File dir;

    // This is the constructor for the OrderReader class.
    public OrderReader() {
        // Set the directory path.
        String dirPath = "exchange";
        // Create a new File object for the directory.
        this.dir = new File(dirPath);
        // Print a message indicating which directory is being read from.
        System.out.println("Reading files from directory: " + dir.getAbsolutePath());
        // Check if the directory exists and is a directory.
        if (!dir.exists() || !dir.isDirectory()) {
            // Print an error message if the directory is invalid.
            System.out.println("Invalid directory: " + dirPath);
        }
    }

    /**
     * This method reads a file and returns a map of orders contained within.
     *
     * @param fileName The name of the file to read.
     */
    public Map<String, LinkedList<Order>> readFile(String fileName) {
        // Create a new File object for the file.
        File file = new File(dir, fileName);
        // Print a message indicating which file is being read.
        System.out.println("Reading file: " + file.getName());
        // Create a new linked list to store the orders.
        LinkedList<Order> orderList = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Read each line of the file.
            while ((line = br.readLine()) != null) {
                // Split the line into fields using a comma as the delimiter.
                String[] fields = line.split(",");
                // Check if there are 4 fields in the line.
                if (fields.length == 4) {
                    // Create a new order object and add it to the list of orders.
                    Order order = new Order(fields[0].trim(),
                            OrderType.type(fields[1].trim()),
                            Integer.parseInt(fields[2].trim()),
                            Integer.parseInt(fields[3].trim()));
                    orderList.add(order);
                }
            }
        } catch (IOException e) {
            // Print a stack trace if an error occurs while reading the file.
            e.printStackTrace();
        }
        // Create a new linked hash map to store the map of orders.
        LinkedHashMap<String, LinkedList<Order>> fileOrderMap = new LinkedHashMap<>(1);
        // Add the order list to the map of orders, with the file name as the key.
        fileOrderMap.put(file.getName(), orderList);
        // Return the map of orders.
        return fileOrderMap;
    }


    /**
     * This method reads all the files in the directory and returns a map of orders contained within.
     *
     * @param alreadyProcessedFiles The names of files that have already been processed.
     *                              If this is a set containing the names of all files in the directory, no files will be processed.
     */
    public Map<String, LinkedList<Order>> readFiles(Collection<String> alreadyProcessedFiles) {
        // Get an array of the file names in the directory.
        String[] fileNames = dir.list();
        // If there are no files in the directory, print an error message and return an empty map.
        if (fileNames == null) {
            System.out.println("No files found in directory: " + dir.getName());
            return Collections.emptyMap();
        }
        // Convert the array of file names to a list and sort it.
        List<String> fileNamesSet = new LinkedList<>(Arrays.asList(fileNames));
        Collections.sort(fileNamesSet);
        // Remove the names of files that have already been processed.
        fileNamesSet.removeAll(alreadyProcessedFiles);
        // If there are no new files to process, return an empty map.
        if (fileNamesSet.isEmpty()) {
            return Collections.emptyMap();
        }
        // Print the number and names of the new files that have been detected.
        System.out.println("Detected new " + fileNamesSet.size() + " files: " + fileNamesSet);

        // Create a new linked hash map to store the map of orders.
        LinkedHashMap<String, LinkedList<Order>> fileOrderMap = new LinkedHashMap<>(fileNamesSet.size());

        // Read each file and add the orders to the map of orders.
        for (String fileName : fileNamesSet) {
            fileOrderMap.putAll(readFile(fileName));
        }

        // Return the map of orders.
        return fileOrderMap;
    }

}
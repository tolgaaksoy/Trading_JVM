package main.java.service.util;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class OrderWriter {

    /**
     * This method writes the trades and order book to a file.
     *
     * @param fileName  The name of the file to write to.
     * @param trades    The trades to write to the file.
     * @param orderBook The order book to write to the file.
     */
    public void writeFile(String fileName, List<String> trades, List<String> orderBook) {
        // Define the file path using the specified file name
        String filePath = "exchange/" + fileName;
        FileWriter writer;
        try {
            // Create a new FileWriter object with the specified file path
            writer = new FileWriter(filePath);

            // Print a separator line
            System.out.println("------------------------------------------------------------");
            // Print the content title
            System.out.println("TXT FILE CONTENT");
            // Print a separator line
            System.out.println("------------------------------------------------------------");

            // Write the trades to the file and print them to the console
            for (String trade : trades) {
                System.out.println(trade);
                writer.write(trade + "\n");
            }

            // Write the order book to the file and print them to the console
            for (String order : orderBook) {
                System.out.println(order);
                writer.write(order + "\n");
            }

            // Print a separator line
            System.out.println("------------------------------------------------------------");

            // Close the writer to free up resources
            writer.close();

        } catch (IOException e) {
            // Handle any exceptions that might occur
            e.printStackTrace();
        }
    }

    /**
     * This method computes the MD5 hash of a file and returns it as a hexadecimal string.
     *
     * @param fileName The name of the file to compute the MD5 hash of.
     * @return The MD5 hash of the file as a hexadecimal string.
     */
    public String checksum(String fileName) throws RuntimeException {
        // Define the file path using the specified file name
        String filePath = "exchange/" + fileName;
        byte[] hash;
        try {
            // Read all the bytes from the file and compute the MD5 hash
            byte[] data = Files.readAllBytes(Paths.get(filePath));
            hash = MessageDigest.getInstance("MD5").digest(data);
        } catch (NoSuchAlgorithmException | IOException e) {
            // Handle any exceptions that might occur
            throw new RuntimeException(e);
        }
        // Convert the hash to a hexadecimal string and return it
        return new BigInteger(1, hash).toString(16);
    }
}

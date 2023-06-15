# Bitvavo Trading JVM Assignment(Details below)

    A basic java based exchange engine that can handle orders and match them with each other. 

## How to run

Requirements:
Java 8 or higher

* Build

```shell
sh build.sh
```

* Run app

```shell
sh run.sh
```
or
```shell
java -jar bitvavo.jar
```

* note: There are some tests included in the project. You can examine the project.

## How it works

    The application is listening ./exchange folder under the project directory for any new files.
    When a new file is created, the application will read the file and process the orders.

## Example

Run:

``` text
    $ sh build.sh    
    $ sh run.sh
```

Test Case 1:

``` text
    $ cat test1.txt 
    10000,B,98,25500
    10005,S,105,20000
    10001,S,100,500
    10002,S,100,10000
    10003,B,99,50000
    10004,S,103,100
    $ ./exchange < test1.txt
```    

*Java Console Output*

``` text
Detected new 1 files: [test1.txt]
Reading file: test1.txt
--------------------------------------------------------------------------------
Processing new file: test1.txt
------------------------------------------------------------
              BIDS | ASKS              
   Quantity  Price | Price     Quantity
     50,000     99 |    100         500
     25,500     98 |    100      10,000
                   |    103         100
                   |    105      20,000

Highest Bid: 99	Lowest Ask: 100
------------------------------------------------------------
------------------------------------------------------------
TXT FILE CONTENT
------------------------------------------------------------
     50,000     99 |    100         500
     25,500     98 |    100      10,000
                   |    103         100
                   |    105      20,000
------------------------------------------------------------
Checksum(MD5): 8ff13aad3e61429bfb5ce0857e846567
--------------------------------------------------------------------------------
```
*Checking file content and md5 after processing*
``` text
    $ cat ./exchange/test1.txt
    50,000     99 |    100         500
    25,500     98 |    100      10,000
                  |    103         100
                  |    105      20,000
    $ md5sum ./exchange/test1.txt
    8ff13aad3e61429bfb5ce0857e846567  ./exchange/test1.txt
``` 
---
Test Case 2:
``` text
    $ cp test1.txt test2.txt
    $ echo "10006,B,105,16000" >> test2.txt 
    $ ./exchange < test2.txt
```    

*Java Console Output*

``` text
Detected new 1 files: [test2.txt]
Reading file: test2.txt
--------------------------------------------------------------------------------
Processing new file: test2.txt
------------------------------------------------------------
Trading 500 units at 100 between 10006(BID) and 10001(ASK).
Trading 10000 units at 100 between 10006(BID) and 10002(ASK).
Trading 100 units at 103 between 10006(BID) and 10004(ASK).
Trading 5400 units at 105 between 10006(BID) and 10005(ASK).
              BIDS | ASKS              
   Quantity  Price | Price     Quantity
     50,000     99 |    105      14,600
     25,500     98 |                   

Highest Bid: 99	Lowest Ask: 105
------------------------------------------------------------
------------------------------------------------------------
TXT FILE CONTENT
------------------------------------------------------------
trade 10006,10001,100,500
trade 10006,10002,100,10000
trade 10006,10004,103,100
trade 10006,10005,105,5400
     50,000     99 |    105      14,600
     25,500     98 |                   
------------------------------------------------------------
Checksum(MD5): ce8e7e5ab26ab5a7db6b7d30759cf02e
--------------------------------------------------------------------------------
```
*Checking file content and md5 after processing*
``` text
    $ cat ./exchange/test2.txt
    trade 10006,10001,100,500
    trade 10006,10002,100,10000
    trade 10006,10004,103,100
    trade 10006,10005,105,5400
        50,000     99 |    105      14,600
        25,500     98 |                   
    $ md5sum ./exchange/test2.txt
    ce8e7e5ab26ab5a7db6b7d30759cf02e  ./exchange/test2.txt
``` 
---
# Order Book Coding Assignment

An exchange allows the buyers and sellers of a product to discover each other and trade. Buyers and sellers (traders)
submit orders to the exchange, and the exchange applies simple rules to determine if a trade has occurred. The dominant
kind of exchange is a central limit order book (CLOB) where orders are matched using ‘price time priority’.

When placing an order, traders specify if they wish to buy or sell, the limit price i.e., the worst possible price they will
trade at, and the quantity (number of shares) they wish to trade. On our exchange trades only occur during the
processing of a newly posted order, and happen immediately, which is known as ‘continuous trading’.

**Matching example**

As orders arrive at the exchange, they are considered for aggressive matching first against the opposite side of the
book.
Once this is completed, any remaining order quantity will rest on their own side of the book.
Consider 3 orders
have been submitted to the exchange, in the following order:	

● Buy 1000 @ 99

● Buy 1200 @ 98

● Buy 500 @ 99

As there are no Sell orders, yet, they rest on the order book as follows (note Buy for 98 is lowest priority):

| Bids (Buying) | Bids (Buying) | Asks (Selling) | Asks (Selling) |
|---------------|---------------|----------------|----------------|
| Quantity      | Price         | Quantity       | Price          |
| 1000          | 99            |                |                |
| 500           | 99            |                |                |
| 1200          | 98            |                |                |

Price time priority refers to the order in which orders in the book are eligible to be matched during the aggressive
phase. Orders are first matched in order of the price (most aggressive to least aggressive), then by arrival time into the
book (oldest to newest). A **Sell** order is now submitted, with a limit price that does not cross with any of the
existing resting orders:

● Sell 2000 @ 101

| Bids (Buying) | Bids (Buying) | Asks (Selling) | Asks (Selling) |
|---------------|---------------|----------------|----------------|
| Quantity      | Price         | Quantity       | Price          |
| 1000          | 99            | 101            | 2000           |
| 500           | 99            |                |                |
| 1200          | 98            |                |                |

A **Sell** order is now submitted that is aggressively-priced: 

● Sell 2000 @ 95

This triggers a matching event as there are orders on the **Buy** side that match with the new **Sell** order.

The orders are matched in price time priority (first by price, then by arrival time into the book) i.e.		

`    ● Buy 1000 @ 99 is matched first (as it is the oldest order at the highest price level)`

`    ● Buy 500 @ 99 is matched second (as it is at the highest price level and arrived after the BUY 1000 @ 99 order)`

`    ● Buy 500 @ 98 is matched third (as it is at a lower price. This partially fills the resting order of 1200, leaving 700 in the order book)`

| Bids (Buying) | Bids (Buying) | Asks (Selling) | Asks (Selling) |
|---------------|---------------|----------------|----------------|
| Quantity      | Price         | Quantity       | Price          |
| 700           | 98            | 101            | 2000           |

### Limit order handling

The assignment is to produce executable code that will accept orders from standard input, and to emit to standard output
the trades as they occur. Once standard input ends, the program should print the final contents of the order book.

Order inputs will be given as a comma separated values, one order per line of the input, delimited by a new line
character.
The fields are: order-id, side, price, quantity.
Side will have a value of ‘B’ for **Buy** or ‘S’ for **sale
**.
Price and quantity will both be integers.
order-id should be handled as a string.

Example 1

In this example, no buyer or seller is willing to pay the opposing price, so no trades occur

``` text
$ cat test1.txt 
10000,B,98,25500
10005,S,105,20000
10001,S,100,500
10002,S,100,10000
10003,B,99,50000
10004,S,103,100
$ ./exchange < test1.txt
     50,000     99 |    100         500
     25,500     98 |    100      10,000
                   |    103         100 
                   |    105      20,000
$
```    
MD5 (output) = 8ff13aad3e61429bfb5ce0857e846567


Which represents the following order book:

| Bids (Buying) | Bids (Buying) | Asks (Selling) | Asks (Selling) |
|---------------|---------------|----------------|----------------|
| Quantity      | Price         | Quantity       | Price          |
| 50000         | 99            | 100            | 500            |
| 25500         | 98            | 100            | 10000          |
|               |               | 103            | 100            |
|               |               | 105            | 20000          |

Example 2

If an order is then submitted to Buy 16000 @ 105p, it will fill completely against the resting orders, producing the
following output:

``` text
$ cp test1.txt test2.txt
$ echo "10006,B,105,16000" >> test2.txt 
$ ./exchange < test2.txt
trade 10006,10001,100,500
trade 10006,10002,100,10000
trade 10006,10004,103,100
trade 10006,10005,105,5400
      50,000     99 |    105      14,600
      25,500     98 |
$
```  

MD5 (output) = ce8e7e5ab26ab5a7db6b7d30759cf02e

Trade output must indicate the aggression order-id, the resting order-id, the price of the match and the quantity
traded, followed by a newline.

The order book output should be formatted to a fixed width using the following format:

000,000,000 000000 | 000000 000,000,000

If a value is too small to cover the whole reserved area, it should be left padded with spaces

Please note that once submitted, orders are not modified by further input. There is no need to maintain more than one
order book, all orders are for the same product.

### Deliverables

The interviewer company will test your application using a variety of inputs. Please pay attention to the expected
output format

Quantities bigger than 999,999,999 will not be provided as input

Prices bigger than 999,999 will not be provided as input

Width truncation of the output will never be required

Performances will not be a central part of the assessment. **Please focus on clean, properly designed, tested, business
sound and reliable production level code.**
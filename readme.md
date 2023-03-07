# Bitvavo Trading JVM Assignment

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

* note : There are some tests included in the project. You can examine in the project.

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
    
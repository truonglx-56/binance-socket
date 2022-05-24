package com.bank.exchange.message;

import lombok.Data;

import java.util.Date;

/**
 *  {
 *             "Exchange": "Binance",
 *             "ForeignName": "POLYBNB",
 *             "Ignore": true,
 *             "Symbol": "POLY"
 *         },
 */
@Data
public class Message {

    private String exchange;
    private String foreignName;
    private Date updateTime;
    private String symbol;
    private String price;
}
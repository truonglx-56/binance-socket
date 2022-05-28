package com.bank.exchange.scraper;

import com.bank.exchange.message.Message;
import com.bank.exchange.model.Symbol;
import com.bank.exchange.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class BinanceScraper {
  private final String URL_BINANCE_V3 = "https://api.binance.com/api/v3/ticker/price?symbol={0}";
  private final List<String> symbols;

  private final RestTemplate restTemplate;

  private final NotificationService notificationService;

  @Autowired
  public BinanceScraper(NotificationService notificationService) {
    this.notificationService = notificationService;
    this.restTemplate = new RestTemplate();
    this.symbols = new ArrayList<>();
  }

  @PostConstruct
  public void init() {
//    this.symbols.add("BTCUSDT");
//    this.symbols.add("ETHUSDT");
//    this.symbols.add("BNBUSDT");
  }

  public void addSymbol(String symbol) {
    if (symbol == null || "".equals(symbol)) return;
    if (!this.symbols.contains(symbol)) this.symbols.add(symbol.toUpperCase(Locale.ROOT));
  }

  @Scheduled(fixedDelay = 3000, initialDelay = 1000)
  public void scheduleFixedRateWithInitialDelayTask() {
    Date date = new Date();
    this.symbols.forEach(symbol -> this.execute(symbol, date));
  }

  private void execute(String symbol, Date date) {
    Symbol response =
        restTemplate.getForObject(MessageFormat.format(URL_BINANCE_V3, symbol), Symbol.class);
    if (response != null) {
      Message message = new Message();
      message.setExchange("Binance");
      message.setPrice(response.getPrice());
      message.setSymbol(response.getSymbol());
      message.setForeignName(symbol);
      message.setUpdateTime(date);

      notificationService.notify(symbol, message);
    }
  }
}

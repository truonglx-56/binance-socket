package com.bank.exchange.controller;

import com.bank.exchange.dto.request.SymbolDTO;
import com.bank.exchange.message.Message;
import com.bank.exchange.scraper.BinanceScraper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange/binance")
@Slf4j
public class BinanceController {

  @Autowired private SimpMessagingTemplate template;

  @Autowired private BinanceScraper binanceScraper;

  @PostMapping(value = "/update/price", consumes = "application/json")
  public void updatePriceBinance(@RequestBody Message message) {

    template.convertAndSend("/topic/updatePrice", message);
  }

  @PostMapping(value = "/update/symbol", consumes = "application/json")
  public void updateSymbolBinance(@RequestBody SymbolDTO symbolDTO) {

    binanceScraper.addSymbol(symbolDTO.getSymbol());
  }
}

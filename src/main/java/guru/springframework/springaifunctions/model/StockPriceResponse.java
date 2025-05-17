package guru.springframework.springaifunctions.model;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.math.BigDecimal;

@JsonClassDescription("Stock price response")
public record StockPriceResponse(@JsonPropertyDescription("The ticker name of the stock") String ticker,
                                 @JsonPropertyDescription("The name of the company") String name,
                                 @JsonPropertyDescription("The price of  the stock") BigDecimal price,
                                 @JsonPropertyDescription("The name of the stock exchange market") String exchange,
                                 @JsonPropertyDescription("The epoch time in of the stock quote") Long updated,
                                 @JsonPropertyDescription("The currency of the stock's value") String currency ) {
}

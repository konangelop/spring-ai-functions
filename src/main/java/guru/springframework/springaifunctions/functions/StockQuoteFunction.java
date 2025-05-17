package guru.springframework.springaifunctions.functions;

import guru.springframework.springaifunctions.model.StockPriceRequest;
import guru.springframework.springaifunctions.model.StockPriceResponse;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

public class StockQuoteFunction implements Function<StockPriceRequest, StockPriceResponse> {

    public static final String STOCK_QUOTE_URL = "https://api.api-ninjas.com/v1/stockprice";
    private final String apiNinjasKey;

    public StockQuoteFunction(String apiNinjasKey) {
        this.apiNinjasKey = apiNinjasKey;
    }

    @Override
    public StockPriceResponse apply(StockPriceRequest stockPriceRequest) {
        RestClient restClient = RestClient.builder()
                .baseUrl(STOCK_QUOTE_URL)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Key", apiNinjasKey);
                    httpHeaders.set("Accept", "application/json");
                    httpHeaders.set("Content-Type", "application/json");
                }).build();

        return restClient.get().uri(uriBuilder -> {
            System.out.println("Building URI for stock quote request: " + stockPriceRequest);

            uriBuilder.queryParam("ticker", stockPriceRequest.ticker());

            return uriBuilder.build();
        }).retrieve().body(StockPriceResponse.class);
    }
}

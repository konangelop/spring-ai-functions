package guru.springframework.springaifunctions.services;


import guru.springframework.springaifunctions.functions.StockQuoteFunction;
import guru.springframework.springaifunctions.functions.WeatherServiceFunction;
import guru.springframework.springaifunctions.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by jt, Spring Framework Guru.
 */
@RequiredArgsConstructor
@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${sfg.aiapp.apiNinjasKey}")
    private String apiNinjasKey;

    private final OpenAiChatModel openAiChatModel;

    @Override
    public Answer getStockQuoteAnswer(Question question) {
        var promptOptions = OpenAiChatOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
                        .function("CurrentStockPrice", new StockQuoteFunction(apiNinjasKey))
                        .description("Get the current stock price for a share")
                        .inputType(StockPriceRequest.class)
                        .responseConverter(response -> {
                            String schema = ModelOptionsUtils.getJsonSchema(StockPriceResponse.class.getGenericSuperclass(), false);
                            String json = ModelOptionsUtils.toJsonString(response);
                            return schema + "\n" + json;
                        })
                        .build()))
                .build();

        Message userMessage = new PromptTemplate(question.question()).createMessage();
        Message systemMessage = new SystemPromptTemplate("You are an agent which returns back a stock price for the given stock symbol (or ticker) including all the information in the response").createMessage();

        var response = openAiChatModel.call(new Prompt(List.of(userMessage, systemMessage), promptOptions));

        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public Answer getWeatherAnswer(Question question) {
        var promptOptions = OpenAiChatOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
                        .function("CurrentWeather", new WeatherServiceFunction(apiNinjasKey))
                        .description("Get the current weather for a location")
                                .inputType(WeatherRequest.class)
                        .responseConverter(response -> {
                            String schema = ModelOptionsUtils.getJsonSchema(WeatherResponse.class.getGenericSuperclass(), false);
                            String json = ModelOptionsUtils.toJsonString(response);
                            return schema + "\n" + json;
                        })
                        .build()))
                .build();

        Message userMessage = new PromptTemplate(question.question()).createMessage();

        Message systemMessage = new SystemPromptTemplate("You are a weather service. You receive weather information from a service which gives you the information based on the metrics system." +
                " When answering the weather in an imperial system country, you should convert the temperature to Fahrenheit and the wind speed to miles per hour. ").createMessage();

        var response = openAiChatModel.call(new Prompt(List.of(userMessage, systemMessage), promptOptions));

        return new Answer(response.getResult().getOutput().getContent());
    }
}

package guru.springframework.springaifunctions.controllers;


import guru.springframework.springaifunctions.model.Answer;
import guru.springframework.springaifunctions.model.Question;
import guru.springframework.springaifunctions.services.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jt, Spring Framework Guru.
 */
@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final OpenAIService openAIService;

    @PostMapping("/weather")
    public Answer askWeatherQuestion(@RequestBody Question question) {
        return openAIService.getWeatherAnswer(question);
    }

    @PostMapping("/stockprice")
    public Answer askStockPriceQuestion(@RequestBody Question question) {
        return openAIService.getStockQuoteAnswer(question);
    }

}

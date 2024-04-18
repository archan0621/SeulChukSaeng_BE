package kr.co.seulchuksaeng.seulchuksaengweb.httpProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class DiscordWebhookSender {

    @Value("${discord.alertUrl}")
    private String url;

    public void sendLog(String title, String message) {

        WebClient webClient = WebClient.create();

        Map<String, Object> param = new HashMap<>();
        param.put("title", title);
        param.put("description", message);

        Map<String, Object> json = new HashMap<>();
        json.put("embeds", Collections.singletonList(param));

        webClient.post()
                .uri(url)
                .body(Mono.just(json), Map.class)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();

        try {
            Thread.sleep(1000); // 1초 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
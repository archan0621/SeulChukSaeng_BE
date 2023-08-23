package kr.co.seulchuksaeng.seulchuksaengweb;

import com.github.archan0621.DiscordLogger;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SeulChukSaengWebApplication {

    @Value("${discord.webhookUrl}")
    private String webhookUrl;

    public static void main(String[] args) {
        SpringApplication.run(SeulChukSaengWebApplication.class, args);
    }

    @PostConstruct
    public void init() {
        DiscordLogger discordLogger = DiscordLogger.instance();
        discordLogger.webhookUrl = webhookUrl;
    }
}

package kr.co.seulchuksaeng.seulchuksaengweb;

import com.github.archan0621.DiscordLogger;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.TimeZone;

@SpringBootApplication
@EnableAspectJAutoProxy //AOP 사용
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
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}

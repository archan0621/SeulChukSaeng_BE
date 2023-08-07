package kr.co.seulchuksaeng.seulchuksaengweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication (exclude = SecurityAutoConfiguration.class)
public class SeulChukSaengWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeulChukSaengWebApplication.class, args);
    }

}

package br.com.vfs.word.information.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class WordInformationCollectorApplication {

    public static void main(String[] args){
        SpringApplication.run(WordInformationCollectorApplication.class, args);
    }

}

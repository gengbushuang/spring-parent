package com.annotation.postprocessor;

import com.annotation.model.Life;
import com.annotation.model.Red;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.annotation.postprocessor")
public class ProcessorConfig {


    @Bean
    public Life red(){
        return new Life();
    }
}

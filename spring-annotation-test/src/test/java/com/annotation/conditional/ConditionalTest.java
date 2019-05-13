package com.annotation.conditional;

import com.annotation.model.Student;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class ConditionalTest {

    @Test
    public void windows() {
        System.getProperties().setProperty("os.name", "Windows");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConditionalConfig.class);

        ConfigurableEnvironment environment = context.getEnvironment();
        String osName = environment.getProperty("os.name");
        System.out.println("os.name----->" + osName);

        String[] beanNamesForType = context.getBeanNamesForType(Student.class);

        for (String name : beanNamesForType) {
            System.out.println(name);
        }
    }

    @Test
    public void linux() {
        System.getProperties().setProperty("os.name", "Linux");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConditionalConfig.class);
        ConfigurableEnvironment environment = context.getEnvironment();
        String osName = environment.getProperty("os.name");
        System.out.println("os.name----->" + osName);

        String[] beanNamesForType = context.getBeanNamesForType(Student.class);

        for (String name : beanNamesForType) {
            System.out.println(name);
        }
    }
}

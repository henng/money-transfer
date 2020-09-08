package com.antgroup.moneytransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * MainLauncher
 * 程序主入口，启动器
 *
 * @author henng
 * @since 2020/9/6
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MainApplication {
    public static void main(String[] args) {
        try {
            SpringApplication application = new SpringApplication(MainApplication.class);
            application.run(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

package com.nexus.contractflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NexusContractFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusContractFlowApplication.class, args);
    }
}

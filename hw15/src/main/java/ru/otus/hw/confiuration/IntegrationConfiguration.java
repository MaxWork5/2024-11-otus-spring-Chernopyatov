package ru.otus.hw.confiuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.IntegrationFlow ;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.domain.SawDust;
import ru.otus.hw.domain.Wood;
import ru.otus.hw.service.CarpentryShopService;
import ru.otus.hw.service.ChemicalShopService;

@Configuration
public class IntegrationConfiguration {
    @Bean
    public MessageChannelSpec<?, ?> woodChannel() {
        return MessageChannels.queue(30);
    }

    @Bean
    public MessageChannelSpec<?, ?> productChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(3);
    }

    @Bean
    public IntegrationFlow flow(CarpentryShopService carpentryShop, ChemicalShopService chemicalShop) {
        return IntegrationFlow.from(woodChannel())
                .split()
                .<Wood, Boolean>route(resource -> resource.weight() > 50,
                        mapping -> mapping
                                .subFlowMapping(true,
                                        subflow -> subflow.handle(carpentryShop, "makeProduct"))
                                .subFlowMapping(false,
                                        subflow -> subflow.<Wood, SawDust>transform(wood -> new SawDust(wood.weight()))
                                                .handle(chemicalShop, "makeProduct")))
                .aggregate()
                .channel(productChannel())
                .get();
    }
}

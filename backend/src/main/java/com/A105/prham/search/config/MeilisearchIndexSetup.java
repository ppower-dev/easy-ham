package com.A105.prham.search.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeilisearchIndexSetup implements ApplicationRunner {

    private final Client meilisearchClient;

    @Override
    public void run(ApplicationArguments args) {
        try {
            Index index = meilisearchClient.index("posts");

            index.updateFilterableAttributesSettings(new String[]{
                    "mmChannelId",
                    "mainCategory",
                    "subCategory",
                    "mmCreatedAt"
            });

            index.updateSortableAttributesSettings(new String[]{
                    "mmCreatedAt"
            });

            index.updateSearchableAttributesSettings(new String[]{
                    "content",
                    "userName"
            });

            log.info("✅ Meilisearch index settings configured successfully");
        } catch (Exception e) {
            log.warn("⚠️ Meilisearch unavailable — skipping index setup: {}", e.getMessage());
        }
    }
}

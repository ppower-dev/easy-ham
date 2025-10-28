package com.A105.prham.common.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticIndexInitializer {

    private final ElasticsearchClient client;
    private static final String INDEX_NAME = "messages";

    @PostConstruct
    public void createIndexIfNotExists() {
        try {
            BooleanResponse exists = client.indices().exists(e -> e.index(INDEX_NAME));
            if (!exists.value()) {
                log.info("Elasticsearch index '{}' not found. Creating new one...", INDEX_NAME);

                String source = """
                    {
                      "settings": {
                        "analysis": {
                          "filter": {
                            "my_synonyms": {
                              "type": "synonym",
                              "synonyms_path": "analysis/synonyms.txt"
                            }
                          },
                          "analyzer": {
                            "my_synonym_analyzer": {
                              "type": "custom",
                              "tokenizer": "nori_tokenizer",
                              "filter": ["lowercase", "my_synonyms"]
                            }
                          }
                        }
                      },
                      "mappings": {
                        "properties": {
                          "cleanedText": {
                            "type": "text",
                            "analyzer": "my_synonym_analyzer"
                          },
                          "channelId": { "type": "keyword" },
                          "createdAt": { "type": "date" },
                          "deadline": { "type": "date" }
                        }
                      }
                    }
                """;

                CreateIndexResponse response = client.indices()
                        .create(c -> c.index(INDEX_NAME).withJson(new java.io.StringReader(source)));

                if (response.acknowledged()) {
                    log.info("✅ Index '{}' created successfully.", INDEX_NAME);
                } else {
                    log.warn("⚠️ Index '{}' creation not acknowledged.", INDEX_NAME);
                }
            } else {
                log.info("Index '{}' already exists. Skipping creation.", INDEX_NAME);
            }

        } catch (Exception e) {
            log.error("❌ Failed to initialize Elasticsearch index '{}': {}", INDEX_NAME, e.getMessage(), e);
        }
    }
}

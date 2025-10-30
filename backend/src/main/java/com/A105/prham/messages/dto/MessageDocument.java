package com.mmA.mymm.messages.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Document(indexName = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDocument {

    @Id
    private String postId;

    @Field(type = FieldType.Keyword)
    private String channelId;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String originalText;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String cleanedText;

    @Field(type = FieldType.Text)
    private String deadline;

    @Field(type = FieldType.Text)
    private String createdAt;
}
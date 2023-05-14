package ru.prime.client.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConfigurationParams {

    private String connectUrl;
    private String subscribeUrl;
    private String topicUrl;
    private String jwt;
}

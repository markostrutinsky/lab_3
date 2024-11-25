package com.strutynskyi.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api.movie.pagination")
public class PaginationConfig {
    private boolean enabled;
    private int defaultPageSize;
    private int maxPageSize;
}

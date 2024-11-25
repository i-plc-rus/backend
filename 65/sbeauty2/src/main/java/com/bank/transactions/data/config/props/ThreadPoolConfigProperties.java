package com.bank.transactions.data.config.props;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@Validated
@Component
@ConfigurationProperties(prefix = "thread-pool.transaction-processing")
public class ThreadPoolConfigProperties {

    /**
     * Property for parallel transaction processing
     */
    @NotNull
    private Integer maxParallelism;

    public Integer getMaxParallelism() {
        return maxParallelism;
    }

    public void setMaxParallelism(@NotNull Integer maxParallelism) {
        this.maxParallelism = maxParallelism;
    }
}

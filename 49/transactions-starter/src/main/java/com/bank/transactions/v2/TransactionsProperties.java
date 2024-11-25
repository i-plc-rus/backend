package com.bank.transactions.v2;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки обработки транзакций
 */
@ConfigurationProperties(prefix = "transactions")
public class TransactionsProperties {

    /**
     * Отображать баннер при старте
     */
    private Boolean showBannerOnStartup = true;
    /**
     * Сумма, при превышении которой, транзакция считается крупной
     */
    private Double largeAmount = 10000.0;

    public Boolean getShowBannerOnStartup() {
        return showBannerOnStartup;
    }

    public void setShowBannerOnStartup(Boolean showBannerOnStartup) {
        this.showBannerOnStartup = showBannerOnStartup;
    }

    public Double getLargeAmount() {
        return largeAmount;
    }

    public void setLargeAmount(Double largeAmount) {
        this.largeAmount = largeAmount;
    }
}

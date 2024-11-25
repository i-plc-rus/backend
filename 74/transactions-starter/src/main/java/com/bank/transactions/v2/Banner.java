package com.bank.transactions.v2;

import com.bank.common.utils.FileUtils;
import jakarta.annotation.PostConstruct;

/**
 * Отображает красивый баннер
 */
public class Banner {

    private final TransactionsProperties properties;

    public Banner(TransactionsProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void postConstruct() {
        if (Boolean.TRUE.equals(properties.getShowBannerOnStartup())) {
            print();
        }
    }

    /**
     * Печать баннера в выходной поток
     */
    public void print() {
        System.out.printf("\n%s\n\n", readBanner());
    }

    private String readBanner() {
        return String.join("\n", FileUtils.readAllLinesFromResource("banner/banner.txt"));
    }
}

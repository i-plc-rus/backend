package ru.competition.transactions.service;

import ru.competition.transactions.model.enums.ProcessorType;

public interface ProcessorStrategy {

    boolean isApplicable(ProcessorType processorType);
}

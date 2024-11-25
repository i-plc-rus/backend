package com.bank.mapper;

import com.bank.model.Transaction;
import com.bank.schema.TransactionRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    Transaction toModel(TransactionRecord record);
}

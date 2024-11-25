package com.bank.mapper;

import com.bank.exception.ExceptionMessage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExceptionMapper {
    default ExceptionMessage toExceptionMessage(Exception exception) {
        return new ExceptionMessage(exception.getClass().getSimpleName(), exception.getMessage());
    }
}

//package com.somecom.model;
//
//import com.somecom.enums.TransactionType;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//
//@Converter
//public class TransactionTypeConverter implements AttributeConverter<TransactionType, Byte> {
//    @Override
//    public Byte convertToDatabaseColumn(TransactionType attribute) {
//        return attribute.getCode();
//    }
//
//    @Override
//    public TransactionType convertToEntityAttribute(Byte dbData) {
//        return TransactionType.of(dbData);
//    }
//}

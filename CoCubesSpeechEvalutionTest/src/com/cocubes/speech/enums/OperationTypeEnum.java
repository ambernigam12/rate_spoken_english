package com.cocubes.speech.enums;

public enum OperationTypeEnum {
    
    Deletion((byte) 35),
    Insertion((byte) 20),
    Substitution((byte) 35),
    CaseErrors((byte) 10),//can be more than one per word
    CaseInsensitiveMatch((byte) 0),
    ExactMatch((byte) 0);
    private final byte weightage;

    OperationTypeEnum(byte weight) {
        this.weightage = weight;
    }

    protected byte Weight() {
        return weightage;
    }
    
}

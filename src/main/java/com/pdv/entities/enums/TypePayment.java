package com.pdv.entities.enums;

public enum TypePayment {
    DINHEIRO(1),
    CREDITO(2),
    DEBITO(3),
    FUNCIONARIO(4),
    OUTROS(5);

    private int code;

    private TypePayment(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TypePayment valueOf(int code) {
        for (TypePayment value : TypePayment.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus Code");
    }
}

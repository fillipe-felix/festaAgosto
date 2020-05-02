package com.pdv.entities.enums;

public enum TypePayment {
    Dinheiro(1),
    Crédito(2),
    Débito(3),
    Funcionário(4),
    Outros(5);

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

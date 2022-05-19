package com.example.fireside.entity;

public enum Month {
    Январь("1"),
    Февраль("2"),
    Март("3"),
    Апрель("4"),
    Май("5"),
    Июнь("6"),
    Июль("7"),
    Август("8"),
    Сентябрь("9"),
    Октябрь("10"),
    Ноябрь("11"),
    Декабрь("12");
    private final String numberOfMonth;

    Month (String numberOfMonth) {
        this.numberOfMonth = numberOfMonth;
    }
    public String getTitle() {
        return this.numberOfMonth;
    }
}

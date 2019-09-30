package br.com.vfs.word.information.collector.enums;

import java.util.Random;

public enum Color {
    ONE("#6FB1FC"),
    TWO("#EDA1ED"),
    THREE("#86B342"),
    FOUR("#F5A45D");

    private String value;
    protected static final Random random = new Random();
    Color(String value) {
        this.value = value;
    }

    public static String radomValue() {
        Color[] values = Color.values();
        int next = random.nextInt(3);
        if(values.length > next) {
            return values[next].value;
        }
        return ONE.value;
    }
}

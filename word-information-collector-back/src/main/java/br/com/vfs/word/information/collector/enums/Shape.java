package br.com.vfs.word.information.collector.enums;

import java.util.Random;

public enum Shape {
    TRIANGLE("triangle"),
    ELLIPSE("ellipse"),
    OCTAGON("octagon"),
    RECTANGLE("rectangle");

    private String value;
    protected static final Random random = new Random();
    Shape(String value) {
        this.value = value;
    }

    public static String radomValue() {
        Shape[] values = Shape.values();
        int next = random.nextInt(3);
        if(values.length > next) {
            return values[next].value;
        }
        return TRIANGLE.value;
    }
}

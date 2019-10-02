package br.com.vfs.word.information.collector.enums;

public enum Color {
    ONE("#6FB1FC"),
    TWO("#EDA1ED"),
    THREE("#86B342"),
    FOUR("#F5A45D");

    private String value;
    Color(String value) {
        this.value = value;
    }

    public static String indexValue(int depth) {
        int index = depth % 4;
        Color[] values = Color.values();
        return values[index].value;
    }
}

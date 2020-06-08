public enum SentimentValue {
    VERY_NEGATIVE(0), NEGATIVE(1), NEUTRAL(2), POSITIVE(3), VERY_POSITIVE(4);

    int index;

    SentimentValue(int index) {
        this.index = index;
    }

    public static SentimentValue fromValue(int value) {
        for (SentimentValue typeSentiment: values()) {
            if (typeSentiment.index == value) {
                return typeSentiment;
            }
        }
        return SentimentValue.NEUTRAL;
    }
}

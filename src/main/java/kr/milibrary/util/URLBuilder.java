package kr.milibrary.util;

public class URLBuilder {
    private final StringBuilder uriBuilder = new StringBuilder();
    private boolean once;

    public URLBuilder(String defaultURL) {
        uriBuilder.append(defaultURL);
    }

    public URLBuilder addUri(String key, String value) {
        if (!once) {
            uriBuilder.append('?');
            once = true;
        } else {
            uriBuilder.append('&');
        }
        uriBuilder.append(String.format("%s=%s", key, value));

        return this;
    }

    public String build() {
        return uriBuilder.toString();
    }
}

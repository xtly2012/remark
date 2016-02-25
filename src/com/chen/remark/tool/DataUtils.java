package com.chen.remark.tool;

/**
 * Created by chenfayong on 16/2/21.
 */
public class DataUtils {

    public static String getFormattedSnippet(String snippet) {
        if (snippet != null) {
            snippet = snippet.trim();
            int index = snippet.indexOf('\n');
            if (index != -1) {
                snippet = snippet.substring(0, index);
            }
        }

        return snippet;
    }
}

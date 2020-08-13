package com.igar15.filecabinet.util;

import java.util.Arrays;
import java.util.Objects;

public class HelperUtil {

    public static boolean checkParamsOnNull(String... params) {
        return Arrays.stream(params)
                .allMatch(Objects::isNull);
    }
}

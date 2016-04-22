package com.sf.module.testutil;

import static org.fest.assertions.api.Assertions.assertThat;

public class EnumHelper {

    public static <T extends Enum<T>> void ensureEnumOrder(Class<T> enumClass, T... values) {
        for (int i = 0; i < values.length; i++) {
            assertThat(values[i].ordinal()).isEqualTo(i);
        }
        assertThat(values.length).isEqualTo(enumClass.getEnumConstants().length);
    }
}
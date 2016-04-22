package com.sf.module.common.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sf.module.common.util.Clock.now;
import static com.sf.module.common.util.StringUtil.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class StringUtilTest {

    @Test
    public void should_ensure_is_blank() {
        assertThat(isBlank("")).isTrue();
        assertThat(isBlank(null)).isTrue();
    }

    @Test
    public void should_ensure_is_not_blank() {
        assertThat(isNotBlank("is not blank")).isTrue();
    }

    @Test
    public void should_get_this_year_with_format_yyyy() {
        //given
        Date now = now();

        //when
        String yyyyMMdd = new SimpleDateFormat("yyyy").format(now);

        //then
        assertThat(getThisYearAsYYYY()).isEqualTo(yyyyMMdd);
    }

    @Test
    public void should_get_this_year_with_specify_month() {
        assertThat(getThisYearWithSpecifyMonth(1)).isEqualTo("201501");
        assertThat(getThisYearWithSpecifyMonth(10)).isEqualTo("201510");
    }
}
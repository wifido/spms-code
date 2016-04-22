package com.sf.module.common.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sf.module.common.util.Clock.TimeRange;
import static org.fest.assertions.api.Assertions.assertThat;

public class ClockTest {
    @Test
    public void should_ensure_is_overlap_between_two_time() {
        //given
        TimeRange firstRange = new TimeRange(800, 1200);
        TimeRange secondRange = new TimeRange(100, 759);
        TimeRange thirdRange = new TimeRange(1201, 1202);
        TimeRange overlapRange = new TimeRange(800, 1200);

        //when
        boolean isOverlapFirst = Clock.isOverlap(firstRange, secondRange);
        boolean isOverlapSecond = Clock.isOverlap(secondRange, thirdRange);
        boolean isOverlap = Clock.isOverlap(firstRange, overlapRange);

        //then
        assertThat(isOverlapFirst).isFalse();
        assertThat(isOverlapSecond).isFalse();
        assertThat(isOverlap).isTrue();
    }

    @Test
    public void should_convert_string_to_date_with_specify_format() throws ParseException {
        //given
        Date yyyyMMdd = new SimpleDateFormat("yyyyMMdd").parse("20140101");

        //when
        Date date = Clock.formatWithYyyyMMdd("20140101");

        //then
        assertThat(date.getTime()).isEqualTo(yyyyMMdd.getTime());
    }

    @Test
    public void should_calculate_time_range_as_day_count_between_two_dates() {
        //given

        //when
        int count = Clock.range("20140101", "20140130");
        int countCrossYear = Clock.range("20141230", "20150130");

        //then
        assertThat(count).isEqualTo(30);
        assertThat(countCrossYear).isEqualTo(32);
    }

    @Test
    public void should_get_day_after_specify_days() {
        //given

        //when
        String afterDay = Clock.afterDay("20140101", 30);

        //then
        assertThat(afterDay).isEqualTo("20140130");
    }

}
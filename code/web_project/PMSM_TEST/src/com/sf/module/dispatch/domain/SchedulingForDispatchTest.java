package com.sf.module.dispatch.domain;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class SchedulingForDispatchTest {

    private static final String FIRST_DAY = "1";

    @Test
    public void should_update_when_work_time_contain_休_with_specify_day() {
        //given
        SchedulingForDispatch schedulingForDispatch = new SchedulingForDispatch();

        //when
        schedulingForDispatch.updateWorkTimeWithExpectDay(FIRST_DAY, "@#@##休#$%#%#");

        //then
        assertThat(schedulingForDispatch.getFirstDay()).isEqualTo("休");
    }

    @Test
    public void should_update_when_work_time_contain_multiple_times() {
        //given
        SchedulingForDispatch schedulingForDispatch = new SchedulingForDispatch();
        schedulingForDispatch.setFirstDay("first-time");

        //when
        schedulingForDispatch.updateWorkTimeWithExpectDay("1", "second-time");

        //then
        assertThat(schedulingForDispatch.getFirstDay()).isEqualTo("first-time" + "<br>" + "second-time");
    }

    @Test
    public void should_update_when_work_time_contain_multiple_times_and_current_time_is_first_one() {
        //given
        SchedulingForDispatch schedulingForDispatch = new SchedulingForDispatch();

        //when
        schedulingForDispatch.updateWorkTimeWithExpectDay("1", "first-time");

        //then
        assertThat(schedulingForDispatch.getFirstDay()).isEqualTo("first-time");
    }
}
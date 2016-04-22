package com.sf.module.common.util;

import com.sf.module.common.util.SchedulingCalculator.Calculator;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;

public class SchedulingCalculatorTest {

    @Test
    public void should_calculate_reset_total() {
        //given
        SchedulingCalculator schedulingCalculator = SchedulingCalculator.getInstance();

        //when
        Calculator calculatorStub = new Calculator<SchedulingStub>() {
            @Override
            public boolean isRest(SchedulingStub schedulingStub) {
                return schedulingStub.time.equals("rest");
            }
        };

        int restDays = schedulingCalculator.getTotalRest(asList(new SchedulingStub(""), new SchedulingStub("rest"), new SchedulingStub("rest")), calculatorStub);

        //then
        assertThat(restDays).isEqualTo(2);
    }

    private static class SchedulingStub {
        public final String time;

        private SchedulingStub(String time) {
            this.time = time;
        }
    }
}
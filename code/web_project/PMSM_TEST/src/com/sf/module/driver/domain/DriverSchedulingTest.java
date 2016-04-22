package com.sf.module.driver.domain;

import com.sf.module.driver.domain.DriverScheduling.Day;
import com.sf.module.testutil.EnumHelper;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.reflect.core.Reflection.field;

public class DriverSchedulingTest {

    public static final String ANY_CODE = "1";

    @Test
    public void should_specify_order_for_day() {
        EnumHelper.ensureEnumOrder(Day.class,
                Day.firstDay,
                Day.secondDay,
                Day.thirdDay,
                Day.fourthDay,
                Day.fifthDay,
                Day.sixthDay,
                Day.seventhDay,
                Day.eighthDay,
                Day.ninthDay,
                Day.tenthDay,
                Day.eleventhDay,
                Day.twelfthDay,
                Day.thirteenthDay,
                Day.fourteenthDay,
                Day.fifteenthDay,
                Day.sixteenthDay,
                Day.seventeenthDay,
                Day.eighteenthDay,
                Day.nineteenthDay,
                Day.twentiethDay,
                Day.twentyFirstDay,
                Day.twentySecondDay,
                Day.twentyThirdDay,
                Day.twentyFourthDay,
                Day.twentyFifthDay,
                Day.twentySixthDay,
                Day.twentySeventhDay,
                Day.twentyEighthDay,
                Day.twentyNinthDay,
                Day.thirtiethDay,
                Day.thirtyFirstDay);
    }

    @Test
    public void should_update_configure_code_with_specify_day() {
        //given
        DriverScheduling driverScheduling = new DriverScheduling();

        //when
        driverScheduling.setConfigureCodeWithSpecifyDay(1, ANY_CODE);

        //then
        assertThat(driverScheduling.getFirstDay()).isEqualTo(ANY_CODE);
    }

    @Test
    public void should_get_config_code_with_specify_day() {
        //given
        DriverScheduling driverScheduling = new DriverScheduling();

        //when
        updateAllDaysConfigCode(driverScheduling);

        //then
        assertAllDaysUpdated(driverScheduling);
    }

    @Test
    public void should_count_total_rest_days() {
        //given
        DriverScheduling driverScheduling = new DriverScheduling();
        updateAllDaysAsRest(driverScheduling);

        //when
        driverScheduling.updateTotalRestDays();

        //then
        int totalRestDays = field("totalRestDays").ofType(int.class).in(driverScheduling).get();
        assertThat(totalRestDays).isEqualTo(31);
    }

    private void updateAllDaysAsRest(DriverScheduling driverScheduling) {
        for (int day = 1; day < 32; day++) {
            driverScheduling.setConfigureCodeWithSpecifyDay(day, "休");
        }

    }

    private void assertAllDaysUpdated(DriverScheduling driverScheduling) {
        for (int day = 0; day < 31; day++) {
            assertThat(Day.values()[day].getConfigureCode(driverScheduling)).isEqualTo(String.valueOf(day));
        }
    }

    private void updateAllDaysConfigCode(DriverScheduling driverScheduling) {
        for (int day = 1; day < 32; day++) {
            driverScheduling.setConfigureCodeWithSpecifyDay(day, String.valueOf(day - 1));
        }
    }
}
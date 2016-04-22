package com.sf.module.driver.domain;

import com.sf.module.driver.domain.DriverWarningModel.MultipleWarning;
import com.sf.module.driver.domain.DriverWarningModel.SingleWarning;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.api.Assertions.assertThat;

public class DriverWarningModelTest {

    private static final int INT = 2015019;
    private static final int INT1 = 20150130;
    private static final int INT2 = 20150129;

    @Test
    public void should_warning_when_single_warning() {
        //given
        DriverWarningModel driverWarningModel = new DriverWarningModel("20141201", "20150101", "employeeCode", "employeeName", "departmentCode", "departmentName", "20141201", "20150101");

        driverWarningModel.putDay("20141201");
        driverWarningModel.putDay("20141202");
        driverWarningModel.putDay("20141203");
        driverWarningModel.putDay("20141204");
        driverWarningModel.putDay("20141205");
        driverWarningModel.putDay("20141206");
        driverWarningModel.putDay("20141207");
        driverWarningModel.putDay("20141208");

        //when
        List<SingleWarning> SingleWarningList = driverWarningModel.singleRangeWarning(0);

        //then
        assertThat(SingleWarningList).hasSize(2);
        assertThat(SingleWarningList.get(0).warningDay).isEqualTo("20141207");
        assertThat(SingleWarningList.get(0).hasWarningNextDay).isFalse();
        assertThat(SingleWarningList.get(0).maxContinuousWorkingDay).isEqualTo(7);

        assertThat(SingleWarningList.get(1).warningDay).isEqualTo("20141208");
        assertThat(SingleWarningList.get(1).hasWarningNextDay).isTrue();
        assertThat(SingleWarningList.get(1).maxContinuousWorkingDay).isEqualTo(8);
    }

    @Test
    public void should_get_serial_number_when_single_warning() {
        DriverWarningModel driverWarningModel = new DriverWarningModel("20141201", "20150101", "employeeCode", "employeeName", "departmentCode", "departmentName", "20141201", "20150101");

        driverWarningModel.putDay("20141201");
        driverWarningModel.putDay("20141202");
        driverWarningModel.putDay("20141203");
        driverWarningModel.putDay("20141204");
        driverWarningModel.putDay("20141205");
        driverWarningModel.putDay("20141206");
        driverWarningModel.putDay("20141207");
        driverWarningModel.putDay("20141208");

        //when
        List<SingleWarning> SingleWarningList = driverWarningModel.singleRangeWarning(0);

        //then
        assertThat(SingleWarningList).hasSize(2);
        assertThat(SingleWarningList.get(0).getSerial()).isEqualTo(1);
    }

    @Test
    public void should_have_warning_next_day_when_next_day_rest() {
        //given
        DriverWarningModel driverWarningModel = new DriverWarningModel("20141201", "20150101", "employeeCode", "employeeName", "departmentCode", "departmentName", "20141201", "20150101");

        driverWarningModel.putDay("20141201");
        driverWarningModel.putDay("20141202");
        driverWarningModel.putDay("20141203");
        driverWarningModel.putDay("20141204");
        driverWarningModel.putDay("20141205");
        driverWarningModel.putDay("20141206");
        driverWarningModel.putDay("20141207");

        driverWarningModel.putDay("20141210");

        //when
        List<SingleWarning> driverWarningList = driverWarningModel.singleRangeWarning(0);

        //then
        assertThat(driverWarningList.get(0).hasWarningNextDay).isTrue();
    }

    @Test
    public void should_not_warning_when_have_not_continuous_working_larger_than_6() {
        //given
        DriverWarningModel driverWarningModel = new DriverWarningModel("20141201", "20150101", "employeeCode", "employeeName", "departmentCode", "departmentName", "20141201", "20150101");
        driverWarningModel.putDay("20141201");
        driverWarningModel.putDay("20141202");
        driverWarningModel.putDay("20141203");
        driverWarningModel.putDay("20141204");
        driverWarningModel.putDay("20141205");
        driverWarningModel.putDay("20141206");

        //when
        MultipleWarning driverWarningList = driverWarningModel.multipleWarning();

        //then
        assertThat(driverWarningList).isEqualTo(MultipleWarning.EMPTY);
    }

    @Test
    public void should_not_exist_any_warning_data() {
        //given
        DriverWarningModel driverWarningModel = new DriverWarningModel("20141201", "20150101"
                , "employeeCode", "employeeName", "departmentCode", "departmentName", "areaCode", "areaName");

        //when
        List<SingleWarning> driverWarningList = driverWarningModel.singleRangeWarning(0);

        //then
        assertThat(driverWarningList).isEmpty();
    }

    @Test
    public void should_specify_value_when_single_warning() {
        //given
        DriverWarningModel driverWarningModel = new DriverWarningModel("20141201", "20150101"
                , "employeeCode", "employeeName", "departmentCode", "departmentName", "areaCode", "areaName");

        driverWarningModel.putDay("20141201");
        driverWarningModel.putDay("20141202");
        driverWarningModel.putDay("20141203");
        driverWarningModel.putDay("20141204");
        driverWarningModel.putDay("20141205");
        driverWarningModel.putDay("20141206");
        driverWarningModel.putDay("20141207");

        //when
        List<SingleWarning> driverWarningList = driverWarningModel.singleRangeWarning(0);
        SingleWarning driverWarning = driverWarningList.get(0);

        //then
        assertThat(driverWarning.employeeCode).isEqualTo("employeeCode");
        assertThat(driverWarning.employeeName).isEqualTo("employeeName");
        assertThat(driverWarning.departmentName).isEqualTo("departmentName");
        assertThat(driverWarning.departmentCode).isEqualTo("departmentCode");
        assertThat(driverWarning.areaCode).isEqualTo("areaCode");
        assertThat(driverWarning.areaName).isEqualTo("areaName");
    }

    @Test
    public void should_calculate_max_continuous_days_when_multiple_months() {
        //given
        DriverWarningModel driverWarningModel = new DriverWarningModel("20141201", "20150228"
                , "employeeCode", "employeeName", "departmentCode", "departmentName", "areaCode", "areaName");

        driverWarningModel.putDay("20141201");
        driverWarningModel.putDay("20141202");
        driverWarningModel.putDay("20141203");
        driverWarningModel.putDay("20141204");
        driverWarningModel.putDay("20141205");
        driverWarningModel.putDay("20141206");
        driverWarningModel.putDay("20141207");

        //when
        MultipleWarning multipleWarning = driverWarningModel.multipleWarning();

        //then
        assertThat(multipleWarning.firstMaxContinuousWorkingDay).isEqualTo(7);
        assertThat(multipleWarning.secondMaxContinuousWorkingDay).isEqualTo(0);
        assertThat(multipleWarning.thirdMaxContinuousWorkingDay).isEqualTo(0);
    }

    @Test
    public void should_calculate_is_continuous_working_as_month() {
        //given
        DriverWarningModel driverWarningModel = new DriverWarningModel("20141201", "20150228"
                , "employeeCode", "employeeName", "departmentCode", "departmentName", "areaCode", "areaName");

        driverWarningModel.putDay("20141201");
        driverWarningModel.putDay("20141202");
        driverWarningModel.putDay("20141203");
        driverWarningModel.putDay("20141204");
        driverWarningModel.putDay("20141205");
        driverWarningModel.putDay("20141206");
        driverWarningModel.putDay("20141207");

        driverWarningModel.putDay("20150101");
        driverWarningModel.putDay("20150102");
        driverWarningModel.putDay("20150103");
        driverWarningModel.putDay("20150104");
        driverWarningModel.putDay("20150105");
        driverWarningModel.putDay("20150106");
        driverWarningModel.putDay("20150107");

        driverWarningModel.putDay("20150201");
        driverWarningModel.putDay("20150202");
        driverWarningModel.putDay("20150203");
        driverWarningModel.putDay("20150204");
        driverWarningModel.putDay("20150205");
        driverWarningModel.putDay("20150206");
        driverWarningModel.putDay("20150207");

        //when
        MultipleWarning multipleWarning = driverWarningModel.multipleWarning();

        //then
        assertThat(multipleWarning.twoMonthContinuous).isTrue();
        assertThat(multipleWarning.threeMonthContinuous).isTrue();
    }

    @Test
    public void should_warning_when_multiple_months_and_have_not_any_data() {
        //given
        DriverWarningModel driverWarningModel = new DriverWarningModel("20141201", "20150228"
                , "employeeCode", "employeeName", "departmentCode", "departmentName", "areaCode", "areaName");

        //when
        MultipleWarning multipleWarning = driverWarningModel.multipleWarning();

        //then
        assertThat(multipleWarning).isEqualTo(MultipleWarning.EMPTY);
        assertThat(multipleWarning.firstMaxContinuousWorkingDay).isEqualTo(0);
        assertThat(multipleWarning.secondMaxContinuousWorkingDay).isEqualTo(0);
        assertThat(multipleWarning.thirdMaxContinuousWorkingDay).isEqualTo(0);
    }

    @Test
    public void should_warn_when_dates_range_with_multiple_segments() {
        //given
        DriverWarningModel driverWarningModel = new DriverWarningModel("20141201", "20150228"
                , "employeeCode", "employeeName", "departmentCode", "departmentName", "areaCode", "areaName");

        driverWarningModel.putDay("20150101");
        driverWarningModel.putDay("20150102");
        driverWarningModel.putDay("20150103");
        driverWarningModel.putDay("20150104");
        driverWarningModel.putDay("20150105");
        driverWarningModel.putDay("20150106");
        driverWarningModel.putDay("20150107");
        driverWarningModel.putDay("20150108");
        driverWarningModel.putDay("20150109");
        driverWarningModel.putDay("20150110");
        driverWarningModel.putDay("20150111");
        driverWarningModel.putDay("20150112");
        driverWarningModel.putDay("20150113");
        driverWarningModel.putDay("20150114");
        driverWarningModel.putDay("20150115");
        driverWarningModel.putDay("20150116");
        driverWarningModel.putDay("20150117");
        driverWarningModel.putDay("20150118");
        driverWarningModel.putDay("20150119");
        driverWarningModel.putDay("20150120");
        driverWarningModel.putDay("20150121");
        driverWarningModel.putDay("20150122");
        driverWarningModel.putDay("20150123");
        driverWarningModel.putDay("20150124");
        driverWarningModel.putDay("20150125");
        driverWarningModel.putDay("20150126");
        driverWarningModel.putDay("20150127");
        driverWarningModel.putDay("20150128");
        driverWarningModel.putDay("20150129");
        driverWarningModel.putDay("20150130");
        driverWarningModel.putDay("20150131");

        //when
        List<SingleWarning> singleWarningList = driverWarningModel.singleRangeWarning(0);

        //then
        assertThat(singleWarningList).hasSize(25);
        assertThat(singleWarningList.get(0).warningDay).isEqualTo("20150107");
        /*assertThat(singleWarningList.get(1).warningDay).isEqualTo("20150115");
        assertThat(singleWarningList.get(2).warningDay).isEqualTo("20150116");
        assertThat(singleWarningList.get(3).warningDay).isEqualTo("20150117");
        assertThat(singleWarningList.get(4).warningDay).isEqualTo("20150118");
        assertThat(singleWarningList.get(5).warningDay).isEqualTo("20150119");

        assertThat(singleWarningList.get(6).warningDay).isEqualTo("20150128");
        assertThat(singleWarningList.get(7).warningDay).isEqualTo("20150129");
        assertThat(singleWarningList.get(8).warningDay).isEqualTo("20150130");*/
    }

    @Test
    public void should_warning_working_total_count() {
        //given
        DriverWarningModel driverWarningModel = new DriverWarningModel("20141101", "20150131"
                , "employeeCode", "employeeName", "departmentCode", "departmentName", "areaCode", "areaName");
        driverWarningModel.putDay("20150101");
        driverWarningModel.putDay("20150102");
        driverWarningModel.putDay("20150103");
        driverWarningModel.putDay("20150104");
        driverWarningModel.putDay("20150105");
        driverWarningModel.putDay("20150106");
        driverWarningModel.putDay("20150107");
        driverWarningModel.putDay("20150108");
        driverWarningModel.putDay("20150109");
        driverWarningModel.putDay("20150110");
        driverWarningModel.putDay("20150111");
        driverWarningModel.putDay("20150112");
        driverWarningModel.putDay("20150113");
        driverWarningModel.putDay("20150114");
        driverWarningModel.putDay("20150115");
        driverWarningModel.putDay("20150116");
        driverWarningModel.putDay("20150117");
        driverWarningModel.putDay("20150118");
        driverWarningModel.putDay("20150119");
        driverWarningModel.putDay("20150120");
        driverWarningModel.putDay("20150121");
        driverWarningModel.putDay("20150122");
        driverWarningModel.putDay("20150123");
        driverWarningModel.putDay("20150124");
        driverWarningModel.putDay("20150125");
        driverWarningModel.putDay("20150126");
        driverWarningModel.putDay("20150127");
        driverWarningModel.putDay("20150128");
        driverWarningModel.putDay("20150129");
        driverWarningModel.putDay("20150130");
        driverWarningModel.putDay("20150131");

        //when
        MultipleWarning multipleWarning = driverWarningModel.multipleWarning();

        //then
        assertThat(multipleWarning.thirdMaxContinuousWorkingDay).isEqualTo(31);
    }

    @Test
    public void should_get_multiple_serial_number_when_single_warning() {
        DriverWarningModel driverWarningModel1 = new DriverWarningModel("20141201", "20150101", "employeeCode", "employeeName", "departmentCode", "departmentName", "20141201", "20150101");

        driverWarningModel1.putDay("20141201");
        driverWarningModel1.putDay("20141202");
        driverWarningModel1.putDay("20141203");
        driverWarningModel1.putDay("20141204");
        driverWarningModel1.putDay("20141205");
        driverWarningModel1.putDay("20141206");
        driverWarningModel1.putDay("20141207");

        DriverWarningModel driverWarningModel2 = new DriverWarningModel("20141201", "20150101", "employeeCode", "employeeName", "departmentCode", "departmentName", "20141201", "20150101");

        driverWarningModel2.putDay("20141201");
        driverWarningModel2.putDay("20141202");
        driverWarningModel2.putDay("20141203");
        driverWarningModel2.putDay("20141204");
        driverWarningModel2.putDay("20141205");
        driverWarningModel2.putDay("20141206");
        driverWarningModel2.putDay("20141207");
        driverWarningModel2.putDay("20141208");
        driverWarningModel2.putDay("20141209");
        driverWarningModel2.putDay("20141210");
        driverWarningModel2.putDay("20141211");
        driverWarningModel2.putDay("20141212");
        driverWarningModel2.putDay("20141213");
        driverWarningModel2.putDay("20141214");

        List<DriverWarningModel> driverWarningModelList = newArrayList();
        driverWarningModelList.addAll(newArrayList(driverWarningModel1, driverWarningModel2));

        //when
        List<SingleWarning> driverWarningArrayList = newArrayList();

        for (DriverWarningModel driverWarningMode : driverWarningModelList) {
            driverWarningArrayList.addAll(driverWarningMode.singleRangeWarning(driverWarningArrayList.size()));
        }

        //then
        assertThat(driverWarningArrayList).hasSize(9);
        assertThat(driverWarningArrayList.get(0).getSerial()).isEqualTo(1);
        assertThat(driverWarningArrayList.get(1).getSerial()).isEqualTo(2);
        assertThat(driverWarningArrayList.get(2).getSerial()).isEqualTo(3);
        assertThat(driverWarningArrayList.get(8).getSerial()).isEqualTo(9);
    }
}
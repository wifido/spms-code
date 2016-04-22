package com.sf.module.dispatch.biz;

import com.sf.module.dispatch.dao.SchedulingForDispatchDao;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.google.common.collect.Maps.newHashMap;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JMockit.class)
public class SchedulingForDispatchBizTest {
    private SchedulingForDispatchBiz schedulingForDispatchBiz;

    private SchedulingForDispatchDao mockedSchedulingForDispatchDao;

    @Before
    public void setup() {
        schedulingForDispatchBiz = new SchedulingForDispatchBiz();
        mockedSchedulingForDispatchDao = mock(SchedulingForDispatchDao.class);
        field("schedulingForDispatchDao").ofType(SchedulingForDispatchDao.class).in(schedulingForDispatchBiz).set(mockedSchedulingForDispatchDao);
    }

    @Test
    @Ignore
    /**
     * Todo:need fix by 053452
     */
    public void should_query_total_size_with_necessary_criteria() {
        //given
        HashMap<String, String> queryCriteria = newHashMap();
        queryCriteria.put("limit", "30");
        queryCriteria.put("start", "1");
        queryCriteria.put("MONTH_ID", "1");
        queryCriteria.put("zoneCode", "755A");

        HashMap<String, String> expectCriteria = newHashMap();
        expectCriteria.put("limit", "30");
        expectCriteria.put("start", "1");
        expectCriteria.put("MONTH_ID", "201401");
        expectCriteria.put("zoneCode", "755A");

        //when
        schedulingForDispatchBiz.queryAll(queryCriteria);

        //then
        verify(mockedSchedulingForDispatchDao).queryTotalSize(expectCriteria);
    }
}
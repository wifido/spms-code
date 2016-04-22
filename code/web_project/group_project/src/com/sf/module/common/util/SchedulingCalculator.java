package com.sf.module.common.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SchedulingCalculator {
    private static SchedulingCalculator instance;

    public static SchedulingCalculator getInstance() {
        if (instance == null) {
            instance = new SchedulingCalculator();
        }
        return instance;
    }

    public <T> int getTotalRest(List<T> objects, final Calculator<T> calculator) {
        Iterable<T> filter = Iterables.filter(objects, new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                return calculator.isRest(t);
            }
        });

        return newArrayList(filter).size();
    }

    public static interface Calculator<SchedulingDomain> {
        public boolean isRest(SchedulingDomain schedulingDomain);
    }
}

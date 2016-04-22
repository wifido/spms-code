package com.sf.module.common.domain;

public enum DayInMonth {
    UNKNOWN,
    first,
    second,
    third,
    fourth,
    fifth,
    sixth,
    seventh,
    eighth,
    ninth,
    tenth,
    eleventh,
    twelfth,
    thirteenth,
    fourteenth,
    fifteenth,
    sixteenth,
    seventeenth,
    eighteenth,
    nineteenth,
    twentieth,
    twentyFirst,
    twentySecond,
    twentyThird,
    twentyFourth,
    twentyFifth,
    twentySixth,
    twentySeventh,
    twentyEighth,
    twentyNinth,
    thirtieth,
    thirtyFirst;

    public String getName() {
        return this.name() + "Day";
    }
}

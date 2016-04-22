create or replace function count_time_distance(beginTime in varchar2,
                                               endTime   in varchar2)
  /**
   * 根据传入的班别上下班时间，计算出排班时长
  **/
  return number is
  beginDate date;
  endDate   date;
  distance  number;
begin
  
  -- 如果开始时间或结束时间等于空 直接返回0
  if beginTime in (null, '') or endTime in (null, '') then
    return 0;
  end if;
  -- 初始化一个日期拼上开始时间和结束时间 算出排班时长。
  beginDate := to_date('2012-02-20 ' || beginTime, 'yyyy-mm-dd hh24:mi');
  endDate   := to_date('2012-02-20 ' || endTime, 'yyyy-mm-dd hh24:mi');
  distance  := ROUND(TO_NUMBER(endDate - beginDate) * 24,2);
  
  -- 如果开始时间大于结束时间 则排班时长加上24小时
  if distance < 0 then
    distance := distance + 24;
  end if;
  
  return distance;
end count_time_distance;

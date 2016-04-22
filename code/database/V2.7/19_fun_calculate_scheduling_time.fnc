create or replace function calculate_scheduling_time(beginTime in varchar2,
                                                     endTime   in varchar2)
  return number is
  beginDate date;
  endDate   date;
  distance  number;
begin
  if beginTime in (null, '') or endTime in (null, '') then
    return 0;
  end if;

  beginDate := to_date('2012-02-20 ' || beginTime, 'yyyy-mm-dd hh24mi');

  if endTime = '2400' then
    endDate := to_date('2012-02-20 ' || '2359', 'yyyy-mm-dd hh24mi');
  else
    endDate := to_date('2012-02-20 ' || endTime, 'yyyy-mm-dd hh24mi');
  end if;

  distance := ROUND(TO_NUMBER(endDate - beginDate) * 24, 2);

  if distance < 0 then
    distance := distance + 24;
  end if;

  return distance;
end calculate_scheduling_time;
/

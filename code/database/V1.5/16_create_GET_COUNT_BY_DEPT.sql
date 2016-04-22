create or replace function GET_COUNT_BY_DEPT(ym            in varchar,
                                                 sf_Date        in date,
                                                 transfer_date in date,
                                                 DATE_FROM     in date,
                                                 dimission_dt in date)RETURN number IS
  days  number(2);
  maxdate date;
begin
  select greatest(nvl(transfer_date, sysdate - 10000),
                  nvl(date_from, sysdate - 10000),
                  nvl(sf_date, sysdate - 10000))
    into maxdate
    from dual;
    days := 0;
  if (maxdate is not null and to_char(maxdate, 'YYYY-MM') = ym) then
    days := to_char(maxdate,'DD');
    end if;
  if (dimission_dt is not null and to_char(dimission_dt, 'YYYY-MM') = ym) then
       days := (SYSDATE - ADD_MONTHS(SYSDATE, -1)) - to_char(dimission_dt,'DD') + 1;
  end if;
  return(days);
end GET_COUNT_BY_DEPT;
/
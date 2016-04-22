create or replace function get_count_by_coincide(maxDay        in varchar,
                                                 minDay        in varchar,
                                                 dimission_dt  in date,
                                                 months        in varchar,
                                                 ym            in varchar,
                                                 sf_Date        in date,
                                                 transfer_date in date,
                                                 DATE_FROM     in date)
  RETURN number IS
  V_USER  number(1);
  maxdate date;
begin
  select greatest(nvl(transfer_date, sysdate - 10000),
                  nvl(date_from, sysdate - 10000),
                  nvl(sf_date, sysdate - 10000))
    into maxdate
    from dual;

  if (dimission_dt is not null and to_char(dimission_dt, 'YYYY-MM') = ym) and
     (months >= to_char(dimission_dt, 'DD')) then
    V_USER := 0;
  elsIF (maxday is null) or (minday is null) THEN
    V_USER := 0;
  elsif (maxdate is not null and to_char(maxdate, 'YYYY-MM') = ym) and
        (months < to_char(maxdate, 'DD')) then
    v_user := 0;
  elsif maxDay = minDay then
    v_user := 1;
  else
    V_USER := 0;
  END IF;
  return(V_USER);
end get_count_by_coincide;
/
create or replace function count_time_distance(beginTime in varchar2,
                                               endTime   in varchar2)
  /**
   * ���ݴ���İ�����°�ʱ�䣬������Ű�ʱ��
  **/
  return number is
  beginDate date;
  endDate   date;
  distance  number;
begin
  
  -- �����ʼʱ������ʱ����ڿ� ֱ�ӷ���0
  if beginTime in (null, '') or endTime in (null, '') then
    return 0;
  end if;
  -- ��ʼ��һ������ƴ�Ͽ�ʼʱ��ͽ���ʱ�� ����Ű�ʱ����
  beginDate := to_date('2012-02-20 ' || beginTime, 'yyyy-mm-dd hh24:mi');
  endDate   := to_date('2012-02-20 ' || endTime, 'yyyy-mm-dd hh24:mi');
  distance  := ROUND(TO_NUMBER(endDate - beginDate) * 24,2);
  
  -- �����ʼʱ����ڽ���ʱ�� ���Ű�ʱ������24Сʱ
  if distance < 0 then
    distance := distance + 24;
  end if;
  
  return distance;
end count_time_distance;

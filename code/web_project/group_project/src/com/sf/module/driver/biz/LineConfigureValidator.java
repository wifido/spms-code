package com.sf.module.driver.biz;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.sf.module.driver.domain.DriveLine;

public class LineConfigureValidator {
	private static LineConfigureValidator instance;
	
	private LineConfigureValidator(){}
	
	public static LineConfigureValidator sharedInstance() {
		if (instance == null) {
			instance = new LineConfigureValidator();
		}
		
		return instance;
	}
	
	public boolean isLegal(List<DriveLine> driveLines) {
		List<String> beginlist = new ArrayList<String>();
		List<String> endlist = new ArrayList<String>();
		for (int i = 0; i < driveLines.size(); i++) {
			beginlist.add(driveLines.get(i).getStartTime());
			endlist.add(driveLines.get(i).getEndTime());
		}
		return !check24Hour(endlist, beginlist);
	}
	
	private static int convertTime(String anyStringAsTimeFormat) {
		return Integer.valueOf(anyStringAsTimeFormat);
	}

	public static boolean check24Hour(List<String> endTimeList, List<String> startTimeList){
		Queue<Integer> queue=new LinkedList<Integer>();
		for(int i=0;i<endTimeList.size();i++){
			queue.offer(convertTime(startTimeList.get(i)));
			queue.offer(convertTime(endTimeList.get(i)));
		}
		return isOver24Hour(queue);
	}
	
	private static boolean isOver24Hour(Queue<Integer> queue){
		Integer firstTime=queue.poll();
		Integer preTime=-1;
		boolean isOver24Hour=false;
		Integer time=firstTime;
		//是否跨天
		while(!queue.isEmpty()){
			preTime=time;
			time=queue.poll();
			if(time<preTime){
				if(time>firstTime){
					return true;
				}
				break;
			}
		}
		//跨天，再判断是否跨24小时
		while(!queue.isEmpty()){
			preTime=time;
			time=queue.poll();
			if(time<preTime||time>firstTime){
				isOver24Hour=true;
				break;
			}
		}
		return isOver24Hour;
	}
	
}

// <%@ page language="java" contentType="text/html; charset=utf-8"%>

function validateStartTime(startTime) {
	if (TIME_2400 == startTime) {
		startTime = '2359';
	}
	return startTime;
}

function validateEndTime(endTime) {
	var result = new Object();
	result.flag = true;
	result.value = endTime;
	if (TIME_0000 == endTime) {
		result.flag = false;
		result.msg = '收车时间不能是00:00!';
		return result;
	}
	if (TIME_2400 == endTime) {
		result.value = TIME_2359;
	}
	return result;
}

function validateTime(startTimeField,endTimeField){
	startTimeField.setValue(validateStartTime(startTimeField.getValue()));
	var result = validateEndTime(endTimeField.getValue());
	if (result.flag) {
		endTimeField.setValue(result.value);
	} else {
		Ext.Msg.alert('提示', result.msg);
		return false;
	}
	return true;
}
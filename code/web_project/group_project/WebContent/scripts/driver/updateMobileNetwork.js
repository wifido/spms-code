// <%@ page language="java" contentType="text/html; charset=utf-8"%>

var updateMobileNetwork = new Ext.Button({
	text: '修改机动配班',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		var selectRecord = grid.getSelectionModel();

//		if (validYeayMonthAfterCurrentTime(selectRecord.getSelected().data.MONTH)) {
//			Ext.Msg.alert('提示', '当月及之前的配班不能进行修改操作');
//			return true;
//		}

		if (selectRecord.getCount() == 0) {
			Ext.Msg.alert('提示', '请选择一条机动配班数据！');
			return;
		}

		if (selectRecord.getCount() > 1) {
			Ext.Msg.alert('提示', '只能选择一条数据！');
			return;
		}

		var record = selectRecord.getSelected();

		if (record.data.TYPE == 1) {
			Ext.Msg.alert('提示', '只能修改机动配班，非机动配班需要通过修改非机动配班按钮实现！');
			return;
		}
		
		if (!Ext.isEmpty(record.data.CONFIGURE_CODE)) {
			Ext.Msg.alert('提示', '已经排班的机动配班不能修改！');
			return true;
		}
		if('0000' == record.data.END_TIME){
			Ext.Msg.alert('提示', '收车时间不能为00:00 !');
			return;
		}
		if('2400' == record.data.END_TIME){
			record.data.END_TIME = '2359';
		}
		if('2400' == record.data.START_TIME){
			record.data.START_TIME = '2359';
		}

		addPanel.setTitle('修改机动配班');
		addPanel.show();

		Ext.getCmp('_yearMonth').setReadOnly(true);
		addFormPanelForMobileNetWork.getForm().setValues({
			_departmentCode: Ext.getCmp('query.deptId').getValue(),
			_startTime: record.data.START_TIME,
			_yearMonth: record.data.MONTH,
			_classCode: record.data.DEPT_CODE + '-' + record.data.CODE,
			_endTime: record.data.END_TIME,
			_validState: record.data.VALID_STATUS,
			configureId: record.data.ID
		});
	}
});
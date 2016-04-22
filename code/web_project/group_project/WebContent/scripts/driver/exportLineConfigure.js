//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var btnExportLineConfigure = new Ext.Button({
	text : "导出",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		exportLineConfigure();
	}
});

function exportLineConfigure() {
	var currentSelected = treePanel.getSelectionModel().getSelectedNode();
	var departmentId = currentSelected.id;
	var code = Ext.getCmp(ID_CODE).getValue();
	var validStatus = Ext.getCmp(ID_VALID_STATUS).getValue();
	var month = Ext.util.Format.date(Ext.getCmp(ID_MONTH).getValue(), "Y-m");
	var myMask = new Ext.LoadMask(centerPanel.getEl(), {
		msg : "正在导出..."
	});
	myMask.show();

	Ext.Ajax.request({
		url : 'export_lineConfigure.action',
		method : 'POST',
		timeout : 60000,
		params : {
			start : 0,
			limit : 60000,
			DEPT_ID : departmentId,
			CODE : code,
			VALID_STATUS : validStatus,
			MONTH : month
		},
		success : function(response) {
			myMask.hide();
			var result = Ext.util.JSON.decode(response.responseText);
			if (Ext.isEmpty(result.error)) {
				Ext.Msg.alert("提示", "导出成功!");
				var url = result.resultMap.downloadPath;
				window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
			} else {
				Ext.Msg.alert("提示", "导出失败!" + result.error);
			}
			myMask.hide();
		},
		failure : function(response) {
			var result = Ext.util.JSON.decode(response.responseText);
			Ext.Msg.alert('提示', "导出失败！" + result.error);
			myMask.hide();
		}
	});

}
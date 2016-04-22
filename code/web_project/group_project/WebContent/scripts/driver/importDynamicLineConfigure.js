//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var dynamic_importTemplateHtml = ['<font style="margin-right:60px;font: 11px Verdana, Arial, Helvetica, sans-serif;">下载模板文件:</font>','<a href="#" onclick="dynamic_downTemplate()">机动配班导入模板.xls</a>'];

var dynamic_downTemplate = function() {
	url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=机动配班导入模板.xls&" + "moduleName=driver&entityName=schedulingMgt&isTemplate=true";
	window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
};

var dynamic_uploadFileName = new Ext.form.Hidden({
	name : "dto.filePath"
});

var dynamic_uploadFile = new Ext.form.TextField({
	width : 350,
	id : 'dynamic_uploadform',
	inputType : 'file',
	name : 'uploadFile',
	fieldLabel : '文件路径',
	labelStyle : 'width:auto'
});

var dynamic_uploadform = new Ext.form.FormPanel({
	height : 160,
	width : 600,
	frame : true,
	fileUpload : true,
	border : false,
	items : [new Ext.Panel({
		layout : 'form',
		border : false,
		items : [{
			xtype : 'hidden',
			id : 'upload_deptid',
			name : "dto.deptId"
		},dynamic_uploadFileName,dynamic_uploadFile]
	}),new Ext.form.Label({
		html : dynamic_importTemplateHtml.join('')
	})]
});

var dynamic_uploadWindow = new Ext.Window({
	title : "导入",
	height : 180,
	width : 500,
	layout : 'column',
	closeAction : 'hide',
	plain : true,
	modal : true,
	items : [dynamic_uploadform],
	tbar : [{
		text : '导入',
		pressed : true,
		height : 18,
		minWidth : 60,
		handler : function() {
			dynamic_uploadFileName.setValue(dynamic_uploadFile.getValue());
			if (dynamic_uploadFileName.getValue().indexOf(".xls") < 0) {
				Ext.MessageBox.alert('提示', '导入时，请先选择Excel文件');
				return false;
			}
			dynamic_submit();
		}
	}]
});

function dynamic_submit() {
	dynamic_uploadform.form.submit({
		waitMsg : '正在提交数据',
		waitTitle : '提示',
		method : "POST",
		timeout : 500000,
		url : "importDynamicLineConfigure_lineConfigure.action",
		success : function(form, action) {
			var result = Ext.util.JSON.decode(action.response.responseText);
			if (!Ext.isEmpty(result.error)) {
				Ext.Msg.alert('提示', "导入失败！" + result.error);
				return;
			}
			var downloadUrl = '';
			var importResult = result.resultMap.importResult;
			var tips = buildTips(importResult);
			var errorDataDownloadPath = importResult.errorDataDownloadPath;
			if (!Ext.isEmpty(errorDataDownloadPath)) {
				errorDataDownloadPath = getDownloadPathWhenUploadFail(errorDataDownloadPath);
				downloadUrl = "  <a href = '#' url='" + errorDataDownloadPath + "' onclick = 'downError(this)'>错误数据下载</a>";
				Ext.MessageBox.alert("提示", tips + downloadUrl, function() {
					dynamic_uploadWindow.hide();
				});
				return;
			}
			Ext.Msg.alert('提示', tips);
			store.reload();
		},
		failure : function(form, action) {
			var result = Ext.util.JSON.decode(action.response.responseText);
			Ext.Msg.alert('提示', "导入失败！" + result.error);
		}
	});
}

var btnImportDynamicLineConfigure = new Ext.Button({
	text : "导入机动配班",
	pressed : true,
	minWidth : 60,
	handler : function() {
		dynamic_uploadWindow.show();
	}
});

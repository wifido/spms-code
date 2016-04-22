//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var exportButton = new Ext.Button({
    text: '导出',
    cls: 'x-btn-normal',
    pressed: true,
    minWidth: 60,
    handler: function () {
        var node = getSelectedDepartmentNode();

        if (hasNotSelectedDepartment(node)) {
            showMessage(MESSAGE_PLEASE_SELECT_SOME_DEPARTMENT);
            return;
        }

        var currentSelected = node || treePanel.getSelectionModel().getSelectedNode();
        var departmentId = currentSelected.id;
        var inputType = getInputType();
        var vehicleNumber = Ext.getCmp(ID_VEHICLE_NUMBER).getValue();

        var isExport = new Ext.LoadMask(centerPanel.getEl(), {msg:"正在导出..."});
        isExport.show();

        Ext.Ajax.request({
            method: 'post',
            url: '../driver/exportLine.action',
            timeout: 60000,
            params: {
                vehicleNumber: vehicleNumber,
                departmentId: departmentId,
                inputType: inputType,
                validStatus: getSelectedValidStatus(),
                configureStatus: Ext.getCmp('configureStatus').getValue()
            },
            success: function (response, config) {
                isExport.hide();
                var obj = Ext.decode(response.responseText);
                window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(obj.fileName));
            },
            failure : function (response) {
                Ext.Msg.alert('提示', '导出出错！');
            }
        });

    }
});
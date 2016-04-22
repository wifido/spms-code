//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var treePanel = new Ext.tree.TreePanel({
    region: 'west',
    margins: '1 1 1 1',
    width: 245,
    title: '网点信息',
    collapsible: true,
    autoScroll: true,
    root: new Ext.tree.AsyncTreeNode({
        id: '0',
        text: '顺丰速运',
        loader: new Ext.tree.TreeLoader({
            dataUrl: "../operation/groupOrgAction.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
        })
    }),
    listeners: {
        beforeclick: function(node, e) {
            Ext.getCmp("query.departmentCode").setValue(node.text.split("/")[0]);
        }
    }
});


var validdepartmentCodeEmpty = function(departmentCode) {
    return Ext.isEmpty(departmentCode);
}

var queryButton = new Ext.Button({
    text: '查询',
    cls: 'x-btn-normal',
    pressed: true,
    minWidth: 60,
    handler: function() {
    	var departmentCode = Ext.getCmp('query.departmentCode').getValue();
        if (validdepartmentCodeEmpty(departmentCode)) {
            Ext.Msg.alert('提示', '网点代码不能为空！');
            return;
        }
        store.setBaseParam("departmentCode", departmentCode);
        store.setBaseParam("yearMonth", Ext.util.Format.date(Ext.getCmp("query.yearMonth").getValue(), "Y-m"));

        store.load({
            params: {
                start: 0,
                limit: 20
            }
        });
    }
});

var exportButton = new Ext.Button({
    text: '导出',
    cls: 'x-btn-normal',
    pressed: true,
    minWidth: 60,
    handler: function() {
        var departmentCode = Ext.getCmp('query.departmentCode').getValue();
        if (validdepartmentCodeEmpty(departmentCode)) {
            Ext.Msg.alert('提示', '网点代码不能为空！');
            return;
        }
        Ext.MessageBox.confirm('请确认', '导出数据过多时可能可能需要较长时间，是否导出', function(button) {
            if (button == 'yes') {
                var exportWaitTitle = new Ext.LoadMask(centerPanel.getEl(), {
                    msg: '正在导出...'
                });
                exportWaitTitle.show();

                Ext.Ajax.request({
                    method: 'POST',
                    url: '../report/exportSchedulingTable.action',
                    timeout: 60000,
                    params: {
                        departmentCode: departmentCode,
                        yearMonth: Ext.util.Format.date(Ext.getCmp("query.yearMonth").getValue(), "Y-m")
                    },success: function(response) {
                        exportWaitTitle.hide();
                        var result = Ext.decode(response.responseText);

                        if (result.success == true) {
                            Ext.Msg.alert('提示', '导出成功!');
                            window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(result.fileName));
                            return;
                        }

                        Ext.Msg.alert('提示', result.error);
                    }
                });
            }
        });
    }
});

var operationButtons = [];
addButton('<app:isPermission code = "/report/querySchedulingTable.action">a</app:isPermission>', queryButton)
addButton('<app:isPermission code = "/report/exportSchedulingTable.action">a</app:isPermission>', exportButton)

function addButton(permisson, button) {
    if (!Ext.isEmpty(permisson)) {
        operationButtons.push('-');
        operationButtons.push(button);
    }
}

var conditionPanel = new Ext.Panel({
    region: 'north',
    height: 120,
    frame: true,
    tbar:operationButtons,
    items: [{
        xtype: 'fieldset',
        title: '查询条件',
        style: 'margin-top:5px;',
        columnWidth: 1,
        frame: true,
        layout: 'column',
        items: [
            {
                layout: 'form',
                columnWidth:.3,
                labelAlign: 'right',
                items: [
                    {
                        xtype: 'textfield',
                        fieldLabel: '网点ID',
                        hidden: true,
                        id: 'query.departmentCode',
                        readOnly: true,
                        anchor: '90%'
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: '网点代码<font color=red>*</font>',
                        id: 'query.departmentCode',
                        readOnly: true,
                        anchor: '90%'
                    }
                ]
            },
            {
                layout: 'form',
                columnWidth:.3,
                labelAlign: 'right',
                items: [
                    {
                        xtype: 'datefield',
                        fieldLabel: '月份<font color=red>*</font>',
                        id: 'query.yearMonth',
                        allowBlank: false,
                        plugins: 'monthPickerPlugin',
                        editable: false,
                        format: 'Y-m',
                        value: new Date(),
                        anchor: '90%'
                    }
                ]
            }
        ]
    }]
});

var record = Ext.data.Record.create([
    {
        name: 'MONTH_ID',
        mapping: 'MONTH_ID',
        type: 'string'
    },
    {
        name: 'AREA_CODE',
        mapping : 'AREA_CODE',
        type: 'string'
    },
    {
        name: 'DEPT_CODE',
        mapping : 'DEPT_CODE',
        type: 'string'
    },
    {
        name: 'EMP_CODE',
        mapping : 'EMP_CODE',
        type: 'string'
    },
    {
        name: 'EMP_NAME',
        mapping : 'EMP_NAME',
        type: 'string'
    },
    {
        name: 'PERSK_TXT',
        mapping : 'PERSK_TXT',
        type: 'string'
    },
    {
        name: 'SF_DATE',
        mapping : 'SF_DATE',
        type: 'string'
    },
    {
        name: 'EMP_STATUS',
        mapping : 'EMP_STATUS',
        type: 'string'
    },
    {
        name: 'SHEDULE_NUM',
        mapping : 'SHEDULE_NUM',
        type: 'string'
    },
    {
        name: 'GROUP_NUM',
        mapping : 'GROUP_NUM',
        type: 'string'
    },
    {
        name: 'PROCESS_NUM',
        mapping : 'PROCESS_NUM',
        type: 'string'
    },
    {
        name: 'LENGTH_TIME_OF_DAY',
        mapping : 'LENGTH_TIME_OF_DAY',
        type: 'string'
    },
    {
        name: 'REST_DAYS',
        mapping : 'REST_DAYS',
        type: 'string'
    },
    {
        name: 'TOTAL_ATTENDANCE',
        mapping : 'TOTAL_ATTENDANCE',
        type: 'string'
    }
]);

var store = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: '../report/querySchedulingTable.action'
    }),
    reader: new Ext.data.JsonReader({
        root: 'resultMap.root',
        totalProperty: 'resultMap.totalSize'
    }, record)
});

var pageButton = new Ext.PagingToolbar({
    store: store,
    displayInfo: true,
    displayMsg: '当前显示 {0} - {1} 总记录数目 {2}',
    pageSize: 20,
    emptyMsg: '未检索到数据'
});

var sm = new Ext.grid.CheckboxSelectionModel({
    singleSelect: false
});

var cm = new Ext.grid.ColumnModel({
    columns: [
        sm,
        {
            header: '月份',
            dataIndex: 'MONTH_ID'
        },
        {
            header: '地区代码',
            dataIndex: 'AREA_CODE'
        },
        {
            header: '网点代码',
            dataIndex: 'DEPT_CODE'
        },
        {
            header: '员工工号',
            dataIndex: 'EMP_CODE'
        },
        {
            header: '员工姓名',
            dataIndex: 'EMP_NAME'
        },
        {
            header: '人员类型',
            dataIndex: 'PERSK_TXT'
        },
        {
            header: '入职时间',
            dataIndex: 'SF_DATE'
        },
        {
            header: '在职状态',
            dataIndex: 'EMP_STATUS',
            renderer: function (value) {
            	if (value == 1){
            		return "在职";
            	}
            	return "离职";
            }
        },
        {
            header: '班别数量',
            dataIndex: 'SHEDULE_NUM'
        },
        {
            header: '小组数量',
            dataIndex: 'GROUP_NUM'
        },
        {
            header: '工序数量',
            dataIndex: 'PROCESS_NUM'
        },
        {
            header: '日均时长',
            dataIndex: 'LENGTH_TIME_OF_DAY'
        },
        {
            header: '休息天数',
            dataIndex: 'REST_DAYS'
        },
        {
            header: '出勤天数',
            dataIndex: 'TOTAL_ATTENDANCE'
        }
    ],
    defaults: {
        sortable: true,
        align: 'center'
    }
});

var resultGrid = new Ext.grid.GridPanel({
    cm: cm,
    sm: sm,
    store: store,
    autoScroll: true,
    loadMask: true,
    tbar: pageButton
});

var centerPanel = new Ext.Panel({
    region: 'center',
    items: [conditionPanel, resultGrid],
    listeners: {
        resize: function(p, adjWidth, adjHeight, rawWidth, rawHeight) {
            resultGrid.setWidth(adjWidth - 5);
            resultGrid.setHeight(adjHeight - 165);
        }
    }
});

// 初始化
Ext.onReady(function() {
    Ext.QuickTips.init();
    Ext.Ajax.timeout = 300000;
    Ext.BLANK_IMAGE_URL = "../ext-3.4.0/resources/images/default/s.gif";
    new Ext.Viewport({
        layout: "border",
        items: [treePanel,centerPanel]
    });
});
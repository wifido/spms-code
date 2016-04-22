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


var validDepartmentCodeEmpty = function(departmentCode) {
    return Ext.isEmpty(departmentCode);
}

var queryButton = new Ext.Button({
    text: '查询',
    cls: 'x-btn-normal',
    pressed: true,
    minWidth: 60,
    handler: function() {
    	var departmentCod = Ext.getCmp('query.departmentCode').getValue();
        if (validDepartmentCodeEmpty(departmentCod)) {
            Ext.Msg.alert('提示', '网点代码不能为空！');
            return;
        }

        store.setBaseParam("departmentCode", departmentCod);
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
        if (validDepartmentCodeEmpty(departmentCode)) {
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
                    url: '../report/export.action',
                    timeout: 60000,
                    params: {
                        departmentCode: departmentCode,
                        yearMonth: Ext.util.Format.date(Ext.getCmp("query.yearMonth").getValue(), "Y-m")
                    },success: function(response) {
                        exportWaitTitle.hide();
                        var result = Ext.decode(response.responseText);

                        if (result.success == true) {
                            Ext.Msg.alert('提示', '导出成功!');
                            window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(result.downloadPath));
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
addButton('<app:isPermission code = "/report/querySchedulingInputStatistical.action">a</app:isPermission>', queryButton)
addButton('<app:isPermission code = "/report/export.action">a</app:isPermission>', exportButton)

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
                        id: 'query.departmentId',
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
        name: 'YEAR_MONTH',
        mapping: 'YEAR_MONTH',
        type: 'string'
    },
    {
        name: 'AREA_CODE',
        mapping : 'AREA_CODE',
        type: 'string'
    },
    {
        name: 'DEPARTMENT_CODE',
        mapping : 'DEPARTMENT_CODE',
        type: 'string'
    },
    {
        name: 'INNER_EMPLOYEE',
        mapping : 'INNER_EMPLOYEE',
        type: 'string'
    },
    {
        name: 'DIURNAL_COUNT',
        mapping : 'DIURNAL_COUNT',
        type: 'string'
    },
    {
        name: 'NON_DIURNAL_COUNT',
        mapping : 'NON_DIURNAL_COUNT',
        type: 'string'
    },
    {
        name: 'SCH_DIURNAL_COUNT',
        mapping : 'SCH_DIURNAL_COUNT',
        type: 'string'
    },
    {
        name: 'SCH_NON_DIURNAL_COUNT',
        mapping : 'SCH_NON_DIURNAL_COUNT',
        type: 'string'
    },
    {
        name: 'OUTER_EMPLOYEE',
        mapping : 'OUTER_EMPLOYEE',
        type: 'string'
    },
    {
        name: 'GROUP_COUNT',
        mapping : 'GROUP_COUNT',
        type: 'int'
    },
    {
        name: 'GROUPING_COUNT',
        mapping : 'GROUPING_COUNT',
        type: 'int'
    },
    {
        name: 'CLASS_COUNT',
        mapping : 'CLASS_COUNT',
        type: 'int'
    },
    {
        name: 'CONFIRM_PROCESS_COUNT',
        mapping : 'CONFIRM_PROCESS_COUNT',
        type: 'int'
    },
    {
        name: 'SCHED_CONFIRM_INNER_EMP_COUNT',
        mapping : 'SCHED_CONFIRM_INNER_EMP_COUNT',
        type: 'int'
    },
    {
        name: 'SCHED_CONFIRM_OUTER_EMP_COUNT',
        mapping : 'SCHED_CONFIRM_OUTER_EMP_COUNT',
        type: 'int'
    },
    {
        name: 'PRO_CONFIRM_INNER_EMP_COUNT',
        mapping : 'PRO_CONFIRM_INNER_EMP_COUNT',
        type: 'int'
    },
    {
        name: 'PRO_CONFIRM_OUTER_EMP_COUNT',
        mapping : 'PRO_CONFIRM_OUTER_EMP_COUNT',
        type: 'int'
    },
    {
        name: 'SCHEDULING_COMPLETE_RATE',
        mapping : 'SCHEDULING_COMPLETE_RATE',
        type: 'string'
    }
]);

var store = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: '../report/queryScheduledInputStatistical.action'
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
            dataIndex: 'YEAR_MONTH'
        },
        {
            header: '地区代码',
            dataIndex: 'AREA_CODE'
        },
        {
            header: '网点代码',
            dataIndex: 'DEPARTMENT_CODE'
        },
        {
            header: '全日制用工(在职人数)',
            dataIndex: 'DIURNAL_COUNT'
        },
        {
            header: '非全日制用工(在职人数)',
            dataIndex: 'NON_DIURNAL_COUNT'
        },
        {
            header: '外包员工',
            dataIndex: 'OUTER_EMPLOYEE'
        },
        {
            header: '小组数量',
            dataIndex: 'GROUP_COUNT'
        },
        {
            header: '已分组人数',
            dataIndex: 'GROUPING_COUNT'
        },
        {
            header: '班别数量',
            dataIndex: 'CLASS_COUNT'
        },
        {
            header: '已确认工序',
            dataIndex: 'CONFIRM_PROCESS_COUNT'
        },
        {
            header: '排班确认(全日制用工)',
            dataIndex: 'SCH_DIURNAL_COUNT'
        },
        {
            header: '排班确认(非全日制用工)',
            dataIndex: 'SCH_NON_DIURNAL_COUNT'
        },
        {
            header: '排班确认（外包）',
            dataIndex: 'SCHED_CONFIRM_OUTER_EMP_COUNT'
        },
        {
            header: '工序安排（内部）',
            dataIndex: 'PRO_CONFIRM_INNER_EMP_COUNT'
        },
        {
            header: '工序安排（外包）',
            dataIndex: 'PRO_CONFIRM_OUTER_EMP_COUNT'
        },
        {
            header: '排班完成率',
            dataIndex: 'SCHEDULING_COMPLETE_RATE',
            renderer: function (value) {
                return value + "%";
            }
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
//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var departmentCod ="";
var deptId="";

var fieldDeptCode = new Ext.form.TextField({width:160,name:"fieldDeptCode", xtype : 'textfield'
	, listeners: {
        specialkey: function(field, e){
            if (e.getKey() == e.ENTER) {
            	queryDeptCode();
            }
        }
    }});

var queryDeptCode = function() {
	if (fieldDeptCode.getValue()=="") {
		Ext.Msg.alert('提示','网点代码不能为空', function(){
			fieldDeptCode.focus();
		});
		return;
	}
	
	Ext.Ajax.request({
		url:"../operation/userDeptAction_queryDeptCode.action",
		params:{fieldDeptCode:fieldDeptCode.getValue()},
		success:function(response){
			var dept_ = Ext.util.JSON.decode(response.responseText);
			var path = dept_.path;
			if(path && path != '/0'){
				treePanel.root.reload();
				treePanel.selectPath(path);
				Ext.getCmp('query.departmentCode').setValue(dept_.deptCode + '/' + dept_.deptName);
				deptCode = dept_.deptCode;
				deptId = dept_.deptId;
				dept_leaf = filterDeptCodeType.indexOf(dept_.typeCode+',') != -1;
				if (dept_leaf) {
					areaCode = dept_.areaCode; 					
				}
				
			} else {
				Ext.Msg.alert('提示','该网点不存在！', function(){
					fieldDeptCode.selectText();
				}, this);
			}
		} ,scope : this
	}, false);
};

var treePanel = new Ext.tree.TreePanel({
    region: 'west',
    margins: '1 1 1 1',
    width: 245,
    title: '网点信息',
    collapsible: true,
    autoScroll: true,
    tbar : [
	        {text : "网点代码", xtype : 'label'}, 
	        fieldDeptCode ,
	        {
	        	icon : "../images/search.gif", 
	        	xtype : 'button', 
	        	style : 'margin-left:2px',
	        	scope : this,
	        	handler : queryDeptCode
	        }], 
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
    	 departmentCod = Ext.getCmp('query.departmentCode').getValue();
        if (validDepartmentCodeEmpty(departmentCod)) {
            Ext.Msg.alert('提示', '网点代码不能为空！');
            return;
        }
        store.setBaseParam("departmentCode", departmentCod);
        store.setBaseParam("dayOfMonth", Ext.util.Format.date(Ext.getCmp("query.dayOfMonth").getValue(), "Y/m/d"));
        
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
                    url: '../report/export_watchkeeperCount.action',
                    timeout: 60000,
                    params: {
                        departmentCode: departmentCode,
                        dayOfMonth: Ext.util.Format.date(Ext.getCmp("query.dayOfMonth").getValue(), "Y/m/d")
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
addButton('<app:isPermission code = "/report/query_watchkeeperCount.action">a</app:isPermission>', queryButton);
addButton('<app:isPermission code = "/report/export_watchkeeperCount.action">a</app:isPermission>', exportButton);

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
        items: [{
                layout: 'form',
                columnWidth:.3,
                labelAlign: 'right',
                items: [{
                        xtype: 'textfield',
                        fieldLabel: '网点ID',
                        hidden: true,
                        id: 'query.departmentId',
                        readOnly: true,
                        anchor: '90%'
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: '<font color=red>网点代码*</font>',
                        id: 'query.departmentCode',
                        readOnly: true,
                        anchor: '90%'
                    }
                ]
            },{
                layout: 'form',
                columnWidth:.3,
                labelAlign: 'right',
                items: [
                    {
                        xtype: 'datefield',
                        fieldLabel: '<font color=red>日期*</font>',
                        id: 'query.dayOfMonth',
                        allowBlank: false,
                        editable: false,
                        format: 'Y/m/d',
                        value: new Date(),
                        anchor: '90%'
                    }
                ]
            }]
    }]
});

var record = Ext.data.Record.create([
    {
        name: 'SHEDULE_DT',
        mapping: 'SHEDULE_DT',
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
        name: 'EMP_DUTY_NAME',
        mapping : 'EMP_DUTY_NAME',
        type: 'string'
    },
    {
        name: 'SHEDULE_TIME',
        mapping : 'SHEDULE_TIME',
        type: 'string'
    },
    {
        name: 'SCHEDULE_CODE',
        mapping : 'SCHEDULE_CODE',
        type: 'string'
    }
]);

var store = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: '../report/query_watchkeeperCount.action'
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
            header: '日期',
            dataIndex: 'SHEDULE_DT'
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
            dataIndex: 'EMP_CODE',
        },
        {
            header: '员工姓名',
            dataIndex: 'EMP_NAME',
        },
        {
            header: '职位',
            dataIndex: 'EMP_DUTY_NAME',
        },
	    {
	      header: '班别时间',
	      dataIndex: 'SHEDULE_TIME'
	    },
        {
            header: '班别代码',
            dataIndex: 'SCHEDULE_CODE'
        }

       ],
    defaults: {
        sortable: true,
        align: 'center'
    }
});

function formatDate(value){
	 var year = value.getUTCFullYear();
	 var month = value.getUTCMonth()+1;//js从0开始取 
	 var date = value.getUTCDate(); 
	 var hour = value.getUTCHours(); 
	 var minutes = value.getUTCMinutes(); 
	 var second = value.getUTCSeconds();
	 
	 if(month<10){
	  month = "0" + month;
	 }
	 if(date<10){
	  date = "0" + date;
	 }
	 if(hour <10){
	  hour = "0" + hour;
	 }
	 if(minutes <10){
	  minutes = "0" + minutes;
	 }
	 if(second <10){
	  second = "0" + second ;
	 }
	 return year+"-"+month+"-"+date+" "+hour+":"+minutes+":"+second;
}
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
//初始化
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout=300000;
	Ext.BLANK_IMAGE_URL="../ext-3.4.0/resources/images/default/s.gif";
	var viewreprot = new Ext.Viewport({
		layout : "border",
		items : [ centerPanel]
	});

});




//中部的Panel
var centerPanel = new Ext.Panel({
	region:'center',
	margins:'1 1 1 0',
	items : [topPanel,grid],
	listeners : {
	  		resize : function(p,adjWidth,adjHeight,rawWidth,rawHeight) {
	  				grid.setWidth(adjWidth-5);
	  				grid.setHeight(adjHeight-165);
	  		}
	  }
});

//顶部的Panel
var topPanel = new Ext.Panel({
	frame : true,
	layout:'column',
	height : 160,
	tbar:['-'
	      <app:isPermission code="/dispatch/dispatchSchedu_querySchedule.action">,btnSearch</app:isPermission>
	      	,'-'
	      <app:isPermission code="/dispatch/dispatchSchedu_saveSchedule.action">,btnAdd</app:isPermission>
	       , '-'
	      <app:isPermission code="/dispatch/dispatchSchedu_updateSchedule.action">,btnEdit</app:isPermission>
	        ,'-'
	      <app:isPermission code="/dispatch/dispatchSchedu_deleteSchedule.action">,btnDelete</app:isPermission>
	      	,'-'
	      <app:isPermission code="/dispatch/dispatchSchedu_exportSchedule.action">,btnExport</app:isPermission>
	      	,'-'
	      <app:isPermission code="/dispatch/dispatchSchedu_scheduleUploadFile.action">,btnImport</app:isPermission>
	],
	items : [{
		xtype:'fieldset',
		title:'查询条件',
		layout:"column",
		columnWidth : 1,
		style : 'margin-top:5px;',
		frame:true,
		items : [{
				  columnWidth : .4,
				  labelWidth:120,
				  labelAlign:'right',
				  layout:'form',
				  items : [{
						xtype:'textfield',
						id:'query.dateId',
						readOnly : true,
						fieldLabel:'<font color=red>网点代码*</font>',
						anchor : '90%'
					}]
		},{
			  columnWidth : .4,
			  labelWidth:120,
			  labelAlign:'right',
			  layout:'form',
			  items : [{
					xtype:'textfield',
					id:'query.employeeCode',
					fieldLabel:'班别名称',
					anchor : '90%'
				}]
	},{
		  columnWidth : .4,
		  labelWidth:120,
		  labelAlign:'right',
		  layout:'form',
		  items : [{
				xtype:'textfield',
				id:'query.createdEmployeeCode',
				fieldLabel:'班别代码',
				anchor : '90%'
			}]
},{
	  columnWidth : .4,
	  labelWidth:120,
	  labelAlign:'right',
	  layout:'form',
	  items : [{
			xtype:'combo',
			id:'query.state',
			typeAhead: true,
		    triggerAction: 'all',
		    lazyRender:true,
		    mode: 'local',
			fieldLabel:'状态查询',
			store : [['10','全部'],['1','有效'],['0','失效']],
			anchor : '90%'
		}]
}]
	}]
});

//查询方法
var querySchedule = function(node){
	var node = node || treePanel.getSelectionModel ().getSelectedNode();
	var deptId = "";
	if(node!=null && node.id!=0){
		deptId = node.id;
	}else{
		Ext.Msg.alert("提示","请先选择网点！");
		return;
	}
	store.setBaseParam("dateId",deptId);
	store.setBaseParam("employeeCode",Ext.getCmp('query.employeeCode').getValue());
	store.setBaseParam("createdEmployeeCode",Ext.getCmp('query.createdEmployeeCode').getValue());
	store.setBaseParam("state", Ext.getCmp('query.state').getValue());
	store.load({
			params : {
					start: 0,
			        limit: 20
			}
	});

}

//复选框
var sm = new Ext.grid.CheckboxSelectionModel({});
//列头构建
var cm =  new Ext.grid.ColumnModel({
	 columns:  [
	 sm,
	 {
	 			header: '月份', sortable: true, dataIndex: 'DATE_ID'
	 },{
	 			header: '排班类型', sortable: true, dataIndex: 'SCHEDULE_ID'
	 },{
	 			header: '创建人', sortable: true, dataIndex: 'CREATED_EMPLOYEE_CODE'
	 },{
	 			header: '修改人',  sortable: true, dataIndex: 'MODIFIED_EMPLOYEE_CODE'
	 }]
});


//表格构建
var grid = new Ext.grid.GridPanel({
	cm : cm,
	sm : sm,
	store : store,
	autoScroll : true,
	loadMask : true,
	tbar : pageBar,
	 viewConfig: {
	        forceFit: true
	 }
});

//分页组件
var pageBar =   new Ext.PagingToolbar({
    store: store,
    displayInfo: true,
    displayMsg : '当前显示 {0} - {1} 总记录数目 {2}',
    pageSize: 20,
   emptyMsg : '未检索到数据'
})


//构建数据存储Store
var store = new Ext.data.Store({
		proxy : new  Ext.data.HttpProxy({
			 url : '../dispatch/dispatchSchedu_querySchedule.action'
		}),
		reader: new  Ext.data.JsonReader({
			root: 'root',
			totalProperty: 'totalSize'
		},record)
});

//数据格构建
var record = Ext.data.Record.create([{
		name : 'DATE_ID' ,
		mapping : 'DATE_ID',
		type : 'date'
	},{
			name : 'SCHEDULE_ID'  ,
			mapping : 'SCHEDULE_ID',
			type : 'string'
	},{
			name : 'CREATED_EMPLOYEE_CODE' ,
			mapping : 'CREATED_EMPLOYEE_CODE',
			type : 'string'
	},{
			name : 'MODIFIED_EMPLOYEE_CODE' ,
			mapping : 'MODIFIED_EMPLOYEE_CODE',
			type : 'string'
	}]);



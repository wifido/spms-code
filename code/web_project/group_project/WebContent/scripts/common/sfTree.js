//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var treeDataUrl = null;
var treeIsCheckBox = false;
var branchDepartmentEditable = false;
var isDispatch = false;
var treeDeptTexts = [];
var treeDeptIds = [];
var treeDeptCodes = [];
var treeDeptCodesWithOutQuotation = [];

function setDeptValue(sfDeptTree) {
	treeDeptTexts = [];
	treeDeptIds = [];
	treeDeptCodes = [];
	treeDeptCodesWithOutQuotation = [];
	var checkedItems = sfDeptTree.getChecked();
	// 遍历获取所有的节点数据
	Ext.each(checkedItems, function(node) {
		if(isDispatch){
			treeDeptTexts.push(node.text);
			treeDeptIds.push(node.id);
			treeDeptCodes.push("'" + (node.text.split('/')[0]) + "'");
			treeDeptCodesWithOutQuotation.push(node.text.split('/')[0]);
			return;
		}
		// 子节点 也就是用户节点
		if (node.leaf || (branchDepartmentEditable && (node.text.indexOf("分部") != -1 || node.text.indexOf("点部") != -1))) {
			treeDeptTexts.push(node.text);
			treeDeptIds.push(node.id);
			treeDeptCodes.push("'" + (node.text.split('/')[0]) + "'");
			treeDeptCodesWithOutQuotation.push(node.text.split('/')[0]);
		}
	});
}

// 这个方法是选择父节点,自动选中所有的子节点
function selParent(node, checked) {
	checked ? node.expand() : node.collapse();
	if (node.hasChildNodes()) {
		node.eachChild(function(child) {
			child.attributes.checked = checked;
			var cb = child.ui.checkbox;
			if (cb) {
				cb.checked = checked;
			}
			selParent(child, checked);
		});
	}
}

// 这个方法是选择子节点,自动选中父节点的父节点
function selChild(node, checked) {
	if (checked) {
		var parentNode = node.parentNode;
		if (parentNode != undefined) {
			if (parentNode.getDepth() != 1) {
				parentNode.attributes.checked = checked;
				var cb = parentNode.ui.checkbox;
				if (cb) {
					cb.checked = checked;
				}
			} else {
				parentNode.attributes.checked = false;
			}
			selChild(parentNode, checked);
		}
	}
}

Ext.sf_dept_window = Ext.extend(Ext.Window, {
	plain : true,
	layout : 'form',
	resizable : true, // 改变大小
	draggable : true, // 不允许拖动
	closeAction : 'close',// 可被关闭 close or hide
	modal : true, // 模态窗口
	width : 400,
	height : 400,
	title : '网点代码',
	buttonAlign : 'right',
	loadMask : true,
	autoScroll : true,
	buttons : [{
		xtype : 'button',
		align : 'right',
		text : '确定',
		handler : function() {
			if (treeIsCheckBox) {
				setDeptValue(this.ownerCt.ownerCt.items.itemAt(0));
				var callBackInput = Ext.get(this.ownerCt.ownerCt.callBackInput);
				var callBakcInputName = callBackInput.getAttribute("name");
				callBackInput.dom.value = treeDeptTexts.join(',');
				if (Ext.get(callBakcInputName + '_id') != null) {
					Ext.get(callBakcInputName + '_id').dom.value = treeDeptIds.join(',');
				}
			}
			this.ownerCt.ownerCt.close();
		}
	},{
		xtype : 'button',
		text : '取消',
		handler : function() {
			this.ownerCt.ownerCt.close();
		}
	}],

	// 在组件初始化期间调用的代码
	initComponent : function() {
		// 初始化清空值
		treeDeptTexts = [];
		treeDeptIds = [];
		treeDeptCodes = [];
		treeDeptCodesWithOutQuotation = [];

		// 判断是否需要checkBox
		if (this.isCheckBox == undefined) {
			treeIsCheckBox = false;
		} else {
			treeIsCheckBox = true;
		}

		// 一线特殊处理，不对网点进行过滤控制
		if(this.isDispatch == undefined) {
			isDispatch = false;
		} else {
			isDispatch = true;
		}
		
		// 是否可以选择点部操作
		if (this.branchDepartmentEditable == undefined) {
			branchDepartmentEditable = false;
		} else {
			branchDepartmentEditable = true;
		}

		// 自己传入 tree URL
		if (this.treeUrl == undefined) {
			treeDataUrl = "../common/getSfDeptTree.action?textField=deptName&idField=id&leafField=&clsField=&childrenField=&isCheckBox=" + this.isCheckBox + "&dataLevel=" + this.dataLevel;
		} else {
			treeDataUrl = this.treeUrl;
		}

		var sfDeptTree = new Ext.tree.TreePanel({
			region : 'west',
			margins : '1 1 1 1',
			title : '网点信息',
			collapsible : true,
			autoScroll : true,
			root : new Ext.tree.AsyncTreeNode({
				id : '0',
				text : '顺丰速运'
			}),
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'sssss',
				listeners : {
					beforeload : function(treeLoader, node) {
						this.dataUrl = treeDataUrl;
					}
				}
			}),
			listeners : {
				beforeclick : function(node, e) {
					if (node != null && node.id != 0 && !treeIsCheckBox) {
						var callBackInput = Ext.get(this.ownerCt.callBackInput);
						treeDeptTexts = [];
						treeDeptIds = [];
						treeDeptCodes = [];
						treeDeptCodesWithOutQuotation = [];
						callBackInput.dom.value = node.text;
						treeDeptTexts.push(node.text);
						treeDeptIds.push(node.id);
						treeDeptCodes.push("'" + (node.text.split('/')[0]) + "'");
						treeDeptCodesWithOutQuotation.push(node.text.split('/')[0]);
					}
				},
				checkchange : function(node, checked) {
					selParent(node, checked);
					//selChild(node, checked);
				}
			}
		});

		// 因为配置对象应用到了“this”，所以属性可以在这里被覆盖，或者添加新的属性
		// （如items,tools,buttons）
		Ext.apply(this, {
			items : [sfDeptTree]
		});

		// 调用父类代码之前

		// 调用父类构造函数（必须）
		Ext.sf_dept_window.superclass.initComponent.apply(this, arguments);

		// 调用父类代码之后
		// 如：设置事件处理和渲染组件
	}
});

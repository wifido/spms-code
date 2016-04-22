/**
 * 调试JS文件加载
 */
Ext.loadDebug = false;

Ext.HOME = "../ext-3.4.0/";
Ext.CMP_IMAGES_HOME = "../ext-3.4.0/sf/images/";
Ext.BLANK_IMAGE_URL = Ext.HOME + "resources/images/default/s.gif";
/**
 * 获取组件图片的URL
 */
Ext.getCmpImagePath = function(imageFileName) {
	return Ext.CMP_IMAGES_HOME + imageFileName;
}

/**
 * Oscar.Xie 对原始Ext进行加强,提供一些便利操作
 */
Ext.HashMap = function() {
	/** Map 大小 * */
	var size = 0;
	/** 对象 * */
	var entry = new Object();

	/** 存 * */
	this.put = function(key, value) {
		if (!this.containsKey(key)) {
			size++;
		}
		entry[key] = value;
	};

	/** 取 * */
	this.get = function(key) {
		return this.containsKey(key) ? entry[key] : null;
	};

	/** 删除 * */
	this.remove = function(key) {
		if (this.containsKey(key) && (delete entry[key])) {
			size--;
		}
	};

	/** 是否包含 Key * */
	this.containsKey = function(key) {
		return (key in entry);
	};

	/** 是否包含 Value * */
	this.containsValue = function(value) {
		for (var prop in entry) {
			if (entry[prop] == value) {
				return true;
			}
		}
		return false;
	};

	/** 所有 Value * */
	this.values = function() {
		var values = new Array();
		for (var prop in entry) {
			values.push(entry[prop]);
		}
		return values;
	};

	/** 所有 Key * */
	this.keys = function() {
		var keys = new Array();
		for (var prop in entry) {
			keys.push(prop);
		}
		return keys;
	};

	/** Map Size * */
	this.size = function() {
		return size;
	};

	/* 清空 */
	this.clear = function() {
		size = 0;
		entry = new Object();
	};
}

Ext.renderDateTime = function(value) {
	if (value) {
		return value.replace('T', ' ');
	}
	return '';
};

Ext.renderDate = function(value) {
	if (value && value.length > 10) {
		return value.substr(0, 10);
	}
	return '';
};

Ext.renderTime = function(value) {
	if (value && value.length > 12) {
		return value.substr(11, value.length);
	}
	return '';
};

Ext.boldFont = function(content) {
	return String.format('<B>{0}</B>', content);
};

//重置文件上传框
Ext.resetFileInput = function(ff){
	if (ff != undefined) {
		if (typeof(ff) == 'string') {
			ff = document.getElementById(ff);
		}
	    var pn = ff.parentNode;
	    var tf = document.createElement("form");
	    pn.replaceChild(tf,ff);
	    tf.appendChild(ff);
	    tf.reset();
	    pn.replaceChild(ff, tf);
	}
}

//默认分页大小
Ext.TypicalPageLimit = 30;

/**
 * 文本工具条动态版
 */
Ext.ToolBarTplTextItem = Ext.extend(Ext.Toolbar.TextItem, {
	initComponent:function(){
		Ext.ToolBarTplTextItem.superclass.initComponent.call(this);
		if (this.defaultText != undefined) {
			this.applyText(this.defaultText);
		}
	},
	defaultText:undefined,
	textTpl:'{0}',
	applyText:function(){
		if (arguments.length > 0) {
			var params = arguments; 
			Ext.ToolBarTplTextItem.superclass.setText.call(this, this.textTpl.replace(/\{(\d+)\}/g, function(m, i){
				if (i < params.length) {
					return params[i];
				}
			}));
		}
	}
});


/**
 * 阿修罗框架,在会话退出时给予提示
 */
Ext.override(Ext.data.Connection, {
    handleResponse : function(response){
	   this.transId = false;
	   var options = response.argument.options;
	   response.argument = options ? options.argument : null;
	   if (response.responseText.indexOf('____isLogin:false____') != -1) {
	      Ext.Msg.alert('msg', response.responseText);
	   } else {
		   this.fireEvent("requestcomplete", this, response, options);
	       if(options.success){
	           options.success.call(options.scope, response, options);
	       }
	       if(options.callback){
	           options.callback.call(options.scope, options, true, response);
	       }
	   }
	}
});

/**
 * 给所有的工具条按钮加上边框
 */
Ext.override(Ext.Button, {
	cls:'x-btn-border'
});

/**
 * 覆盖JsonWriter提交时候的参数名称
 */
Ext.override(Ext.data.JsonWriter, {
	render : function(params, baseParams, data) {
        if (this.encode === true) {
            // Encode here now.
            Ext.apply(params, baseParams);
            params[this.rootParams || this.meta.root] = Ext.encode(data);
        } else {
            // defer encoding for some other layer, probably in {@link Ext.Ajax#request}.  Place everything into "jsonData" key.
            var jdata = Ext.apply({}, baseParams);
            jdata[this.rootParams || this.meta.root] = data;
            params.jsonData = jdata;
        }
    }
});

/**
 * 禁止列自动排序
 */
Ext.override(Ext.grid.PropertyGrid, {
	initComponent : function(){
        this.customRenderers = this.customRenderers || {};
        this.customEditors = this.customEditors || {};
        this.lastEditRow = null;
        var store = new Ext.grid.PropertyStore(this);
        this.propStore = store;
        var cm = new Ext.grid.PropertyColumnModel(this, store);
        if (this.autoSort) {
        	store.store.sort('name', 'ASC');
        }
        this.addEvents(
            'beforepropertychange',
            'propertychange'
        );
        this.cm = cm;
        this.ds = store.store;
        Ext.grid.PropertyGrid.superclass.initComponent.call(this);

		this.mon(this.selModel, 'beforecellselect', function(sm, rowIndex, colIndex){
            if(colIndex === 0){
                this.startEditing.defer(200, this, [rowIndex, 1]);
                return false;
            }
        }, this);
    }
});

/**
 * 加速GridView删除操作
 */
Ext.override(Ext.grid.GridView, {
	focusRemoved : false,
	removeRow : function(row) {
        Ext.removeNode(this.getRow(row));
        if (this.focusRemove) {
        	this.syncFocusEl(row);
        }
    },
    removeRows : function(firstRow, lastRow) {
        var bd = this.mainBody.dom,rowIndex;
            
        for (rowIndex = firstRow; rowIndex <= lastRow; rowIndex++){
            Ext.removeNode(bd.childNodes[firstRow]);
        }
        
        if (this.focusRemove) {
        	this.syncFocusEl(firstRow);
        }
    }
});

/**
 * 给所有的工具条按钮加上边框
 */
Ext.override(Ext.form.TextField, {
	initComponent : function(){
		if (this.allowBlank === false) {
			this.labelSeparator = '<span style="color:#FF0000;">*</span>:';
		}
        Ext.form.TextField.superclass.initComponent.call(this);
        this.addEvents(
            'autosize',
            'keydown',
            'keyup',
            'keypress'
        );
    }
});

/**
 * 给所有设置为readOnly的输入红色显示
 */
Ext.override(Ext.form.Field, {
	getAutoCreate : function(){
		var cfg = Ext.isObject(this.autoCreate) ?
		              this.autoCreate : Ext.apply({}, this.defaultAutoCreate);
		// 文件域只读
		if( this.inputType=='file' ){
			cfg.onkeydown='return false;'
		}
		if(this.id && !cfg.id){
		    cfg.id = this.id;
		}
		return cfg;
	},
	setReadOnly : function(readOnly){
        if(this.rendered){
            this.el.dom.readOnly = readOnly;
            if (readOnly) {
		        	this.el.setStyle('color', "#FF0000");
		        } else {
		        	this.el.setStyle('color', "#000000");
		        }
        }
        this.readOnly = readOnly;
    }
});

/**
 * Grid的单元格可选
 */
Ext.override(Ext.grid.GridView, {
	cellTpl: new Ext.Template(
        '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} {css} x-grid3-cell-select-on" style="{style}" tabIndex="0" {cellAttr}>',
            '<div class="x-grid3-cell-inner x-grid3-col-{id}" unselectable="on" {attr}>{value}</div>',
        '</td>'
    )
});

/**
 * 窗口帮助类
 */
Ext.WindowHelper = {
	getWindowWidth:function() {
		return document.documentElement.clientWidth;
	},
	getWindowHeight:function() {
		return document.documentElement.clientHeight;
	},
	getWidthWithPercent:function(decimal) {
		return this.getWindowWidth()*this.checkDecimal(decimal);
	},
	getHeightWithPercent:function(decimal) {
		return this.getWindowHeight()*this.checkDecimal(decimal);
	},
	checkDecimal:function(decimal) {
		if (decimal < 0.1) {
			return 0.1;
		} else if (decimal > 0.9) {
			return 0.9;
		}
		return decimal;
	}
};

//扩展Ext.MessageBox,增加showErr方法
Ext.MessageBox.showErr = Ext.Msg.showErr = function(msg) {
	Ext.MessageBox.show({
		title : "${app:i18n_def('ext.js.1','出错了')}",
		msg : msg,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.ERROR
	});
};

Ext.MessageBox.showTip = Ext.Msg.showTip = function(msg) {
	Ext.MessageBox.alert('${app:i18n_def('ext.js.2','提示')}', msg);
};

Ext.MessageBox.showConfirm = Ext.Msg.showConfirm = function(msg, callback, scope) {
	Ext.MessageBox.confirm('${app:i18n_def('ext.js.2','提示')}', msg, callback, scope || this);
};

Ext.showSuccessTip = function(){
	Ext.Msg.showTip("${app:i18n_def('ext.js.3','操作成功!')}");
};

/**
 * Ajax请求滚动条
 */
Ext.AjaxMask = Ext.extend(Ext.util.Observable, {
	constructor:function(el){
		Ext.AjaxMask.superclass.constructor.call(this, {});
		this.dom = Ext.DomHelper.createDom({
			tag:'div',
			cls:'sf-ajax-mask'
		});
		
		if (typeof(el) == 'string') {
			Ext.get(el).appendChild(this.dom);
		} else {
			el.appendChild(this.dom);
		}
		this.hide();
	},
	show:function() {
		this.dom.style.display="block";
	},
	hide:function() {
		this.dom.style.display="none";
	}
});

//扩展Ext.Ajax,增加requestEx方法
(function(){
	var initOpt = {
		failure:function (rsp, opt){
			var msg = '';
			if (rsp.status == 0) {
				msg = "${app:i18n_def('ext.js.4','连接失败,无法连接到服务器!')}";
			} else if (rsp.status == -1) {
				var t = rsp.timeout || Ext.Ajax.timeout;
				msg = String.format("${app:i18n_def('ext.js.5','连接超时,服务器在{0}秒内没有响应!')}", Math.round(t / 1000));
			} else {
				var r = Ext.decode(rsp.responseText);
				if (r.error) {
					msg = r.error;
					if (msg.length > 1000) {
						msg = msg.substring(0, 1000);
					}
					String.format("${app:i18n_def('ext.js.6','错误代码:{0}, 错误描述:{1}, 错误详情: {2}')}", rsp.status, rsp.statusText, msg);
				} else {
					String.format("${app:i18n_def('ext.js.7','错误代码:{0}, 错误描述:{1}')}", rsp.status, rsp.statusText);
				}
			}
			Ext.MessageBox.showErr(msg);
		},
		success:function(rsp, opt) {
			var result = Ext.decode(rsp.responseText);
			if (result.success == undefined || result.success!=false) {
				if (opt.successCallback) {
					opt.successCallback.call(opt.scope || this, result, rsp, opt);
				}
			} else {
				var msg = "${app:i18n_def('ext.js.8','操作失败,服务器出现异常.')}";
				var er = result.error;
				if (er) {
					if (er.length > 1000) {
						er = er.substring(0, 1000);
					}
					msg += er;
				}
				Ext.MessageBox.showErr(msg);
			}
		},
		callback:function(){
			if (Ext.Ajax.requestMask) {
				Ext.Ajax.requestMask.hide();
			}
		},
		successCallback:Ext.showSuccessTip
	};
	
	Ext.Ajax.requestEx = function(opt){
		if (!Ext.Ajax.requestMask) {
			Ext.Ajax.requestMask = new Ext.AjaxMask(Ext.getBody());
		}
		Ext.Ajax.requestMask.show();
		if (opt.callback) {
			opt.callback = opt.callback.createInterceptor(initOpt.callback);
		}
		Ext.Ajax.request(Ext.applyIf(opt, initOpt));
	};
})();

//扩展表单提交
Ext.override(Ext.form.BasicForm, {
	submitEx:function(opt) {
		if (!opt) {
			opt = {};
		}
		var obj = Ext.apply({
			waitTitle:"${app:i18n_def('ext.js.9','请稍后')}",
			waitMsg:"${app:i18n_def('ext.js.10','正在执行操作...')}",
			failure:this.submitFailure,
			success:this.submitSuccess
		}, opt);
		
		Ext.applyIf(opt, this.successCallbackCfg);
		
		Ext.apply(obj, {
			timeout : opt.timeout || (5 * 60),
			scope:opt
		});
		
		this.submit(obj);
	},
	submitSuccess : function(form, action) {
		if (form.fileUpload && !action.result) {
			Ext.Msg.showErr("${app:i18n_def('ext.js.4','连接失败,无法连接到服务器!')}");
		} else {
			this.successCallback.call(this.scope || this, action.result, form, action);
		}
	},
	submitFailure : function(form, action){
		var msg = '';
		if (action.failureType === Ext.form.Action.CONNECT_FAILURE) {
			if (action.response.status == 0) {
				msg = "${app:i18n_def('ext.js.4','连接失败,无法连接到服务器!')}";
			} else {
				msg = String.format("${app:i18n_def('ext.js.7','错误代码:{0}, 错误描述:{1}')}", action.response.status, action.response.statusText);
			}
		} else if (action.failureType === Ext.form.Action.SERVER_INVALID) {
			if (action.result.error) {
				msg = action.result.error;
				if (msg.length > 1000) {
					msg = msg.substring(0, 1000);
				}
			}
		} else if (action.failureType === Ext.form.Action.CLIENT_INVALID) {
			msg = "${app:i18n_def('ext.js.11','请将表单填写正确!')}";
		}
		
		Ext.MessageBox.showErr(msg);
	},
	successCallbackCfg : {
		successCallback:Ext.showSuccessTip
	}
});

/**
 * 扩展store.load后的后置处理
 */
Ext.override(Ext.data.Store, {
	loadEx:function(opt) {
		if (!opt) {
			opt = {};
		}
		
		var obj = Ext.apply({}, opt);
		if (!obj.callback) {
			obj.callback = this.callbackEx;
			obj.scope = {
				reader:this.reader,
				successCallbackScope:opt.successCallbackScope || this 
			};
		}
		
		this.load(obj);
	},
	callbackEx:function(records, options, s) {
		var success = true;
		if(this.reader.jsonData) {
			var msg = this.reader.jsonData.error;
			if (msg && msg != null) {
				if (msg.length > 1000) {
					msg = msg.substring(0, 1000);
				}
				Ext.Msg.showTip(msg);
				success = false;
			}
		}
		if (options.successCallback) {
			options.successCallback.call(this.successCallbackScope, success, records);
		}
	}
});

if (Ext.loadDebug) {
	alert('ext.js load ok!');
}


/**
 * begin 扩展 Ext.tree.TreePanel
 * to 3)
 */
SupportSearchTreePanel = Ext.extend(Ext.tree.TreePanel, {
	initComponent : function() {
		
		var deptLoader = new Ext.tree.TreeLoader({
			url : "../tcmscommon/deptTree.action?textField=deptName&idField=id&leafField=&clsField=&childrenField=",
			dataUrl : "../tcmscommon/deptTree.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
		});
		
		this.deptRoot = new Ext.tree.AsyncTreeNode({
			text : "${app:i18n_def('share.js.2','顺丰速运')}",
			id : "0"
		});
		
		this.fieldDeptCode = new Ext.form.TextField({width:116,name:"fieldDeptCode", xtype : 'textfield'
			, listeners: {
                specialkey: function(field, e){
                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
                    if (e.getKey() == e.ENTER) {
                    	this.queryDeptCode();
                    }
                },
                scope : this
            }});
		
		Ext.apply(this, {
			width : 200,
			region : 'west',
			title : '${app:i18n_def('share.js.3','网点信息')}',
			autoScroll : true,
			split : true,
			listeners : this.listeners_,
			collapsible : true,
			loader : deptLoader,
			tbar : [{text : "${app:i18n_def('share.js.7','网点代码')}", xtype : 'label'}, this.fieldDeptCode 
			, {icon : "../images/cmscommon/search.gif", xtype : 'button', style : 'margin-left:2px',scope : this, handler : this.queryDeptCode} ],
			root : this.deptRoot
		});
		SupportSearchTreePanel.superclass.initComponent.call(this);
	},
	queryDeptCode : function() {
		if (this.fieldDeptCode.getValue()=="") {
			Ext.Msg.alert('${app:i18n_def('share.js.4','提示')}:','${app:i18n_def('share.js.5','网点代码不能为空')}', function(){
				this.fieldDeptCode.focus();
			}, this);
			return;
		}
		Ext.Ajax.request({
			url:"../tcmscommon/userDeptAction_queryDeptCode.action",
			params:{fieldDeptCode:this.fieldDeptCode.getValue()},
			success:function(response){
				var dept_ = Ext.util.JSON.decode(response.responseText);
				var path = dept_.path;
				if(path && path!='/0'){
					this.deptRoot.reload();
					this.selectPath(path);
					var strId = path.substring(path.lastIndexOf('/')+1,path.length);
					if (this.listeners_ && this.listeners_.click) {
						this.listeners_.click.call(this.listeners_.scope, {text : dept_.deptCode + '/' +dept_.deptName, id : strId, attributes : {deptCode : dept_.deptCode, typeLevel : dept_.typeLevel}});
					}
				} else {
					Ext.Msg.alert('${app:i18n_def('share.js.4','提示')}:','${app:i18n_def('share.js.6','该网点不存在')}', function(){
						this.fieldDeptCode.selectText();
					}, this);
				}
			} ,scope : this
		});
	}
});
Ext.reg('SupportSearchTreePanel', SupportSearchTreePanel) ;
/**
 * end 扩展 Ext.tree.TreePanel
 * to 3)
 */
if (Ext.isIE) {
	if  (!Ext.grid.GridView.prototype.templates) {   
		Ext.grid.GridView.prototype.templates = {};   
	}   
	Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(   
			'<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,   
			'<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,   
			'</td>'   
	);
}


//设置总宽度
Ext.grid.ColumnModel.override({
  getTotalWidth : function(includeHidden) {
  	var off = 0;
  	if(Ext.isChrome){
  	   off=2;
  	}
      if (!this.totalWidth) {
          this.totalWidth = 0;
          for (var i = 0, len = this.config.length; i < len; i++) {
              if (includeHidden || !this.isHidden(i)) {
                  this.totalWidth += this.getColumnWidth(i)+off;
              }
          }
      }
      return this.totalWidth;
  }
});
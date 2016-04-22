/**
 * 调试JS文件加载
 */
Ext.loadDebug = false;

Ext.HOME = "../ext-3.4.0/";
Ext.BLANK_IMAGE_URL = Ext.HOME + "resources/images/default/s.gif";

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
	   if (response.responseText.indexOf('____isLogin:false____') != -1
	           || response.responseText.indexOf('____isForbidden:true____') != -1) {
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
 * 给所有设置为readOnly的输入灰色显示
 */
Ext.override(Ext.form.Field, {
    setReadOnly : function(readOnly) {
        if (this.rendered) {
            this.el.dom.readOnly = readOnly;
            if (readOnly) {
                this.el.setStyle('color', "#C0C0C0");
            } else {
                this.el.setStyle('color', "#000000");
            }
        }
        this.readOnly = readOnly;
    }
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
 * 把只读的输入框的label的*号，变成红色
 */
Ext.override(Ext.form.TextField, {
	initComponent : function(){
		if (this.allowBlank === false) {
			if (this.fieldLabel && (this.fieldLabel.indexOf("*") < 0)) {
				this.labelSeparator = '<span style="color:#FF0000;">*</span>:';
			}
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
		title : "出错了",
		msg : msg,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.ERROR
	});
};

Ext.MessageBox.showTip = Ext.Msg.showTip = function(msg) {
	Ext.MessageBox.alert('提示', msg);
};

Ext.MessageBox.showConfirm = Ext.Msg.showConfirm = function(msg, callback, scope) {
	Ext.MessageBox.confirm('提示', msg, callback, scope || this);
};

Ext.showSuccessTip = function(){
	Ext.Msg.showTip("操作成功!");
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
				msg = "连接失败,无法连接到服务器!";
			} else if (rsp.status == -1) {
				var t = rsp.timeout || Ext.Ajax.timeout;
				msg = String.format("连接超时,服务器在{0}秒内没有响应!", Math.round(t / 1000));
			} else {
				var r = Ext.decode(rsp.responseText);
				if (r.error) {
					msg = r.error;
					if (msg.length > 1500) {
						msg = msg.substring(0, 1500);
					}
					String.format("错误代码:{0}, 错误描述:{1}, 错误详情: {2}", rsp.status, rsp.statusText, msg);
				} else {
					String.format("错误代码:{0}, 错误描述:{1}", rsp.status, rsp.statusText);
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
				var msg = "操作失败,服务器出现异常";
				var er = result.error;
				if (er) {
					if (er.length > 1500) {
						er = er.substring(0, 1500);
					}
					msg = er;
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
			waitTitle:"请稍后",
			waitMsg:"正在执行操作...",
			failure:this.submitFailure,
			success:this.submitSuccess
		}, opt);
		
		Ext.applyIf(opt, this.successCallbackCfg);
		
		Ext.apply(obj, {
			scope:opt
		});
		
		this.submit(obj);
	},
	submitSuccess : function(form, action) {
		if (form.fileUpload && !action.result) {
			Ext.Msg.showErr("连接失败,无法连接到服务器!");
		} else {
			this.successCallback.call(this.scope || this, action.result, form, action);
		}
	},
	submitFailure : function(form, action){
		var msg = '';
		if (action.failureType === Ext.form.Action.CONNECT_FAILURE) {
			if (action.response.status == 0) {
				msg = "连接失败,无法连接到服务器!";
			} else {
				msg = String.format("错误代码:{0}, 错误描述:{1}", action.response.status, action.response.statusText);
			}
		} else if (action.failureType === Ext.form.Action.SERVER_INVALID) {
			if (action.result.error) {
				msg = action.result.error;
				if (msg.length > 1500) {
					msg = msg.substring(0, 1500);
				}
			}
		} else if (action.failureType === Ext.form.Action.CLIENT_INVALID) {
			msg = "请将表单填写正确!";
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
				if (msg.length > 1500) {
					msg = msg.substring(0, 1500);
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

/*修复办法，谷歌浏览器中,table的单元格实际宽度=指定宽度+padding，所以只要重写gridview里的一个方法*/
if (Ext.isChrome) {
    Ext.override(Ext.grid.GridView, {
        getColumnStyle : function(colIndex, isHeader) {
            var colModel = this.cm;
            colConfig = colModel.config;
            style = isHeader ? '' : colConfig[colIndex].css || '';
            align = colConfig[colIndex].align;
            style += String.format("width: {0};", parseInt(this.getColumnWidth(colIndex)) - 2
                    + 'px');
            if (colModel.isHidden(colIndex)) {
                style += 'display: none; ';
            }
            if (align) {
                style += String.format("text-align: {0};", align);
            }
            return style;
        }
    });
}

if (Ext.loadDebug) {
	alert('ext.js load ok!');
}
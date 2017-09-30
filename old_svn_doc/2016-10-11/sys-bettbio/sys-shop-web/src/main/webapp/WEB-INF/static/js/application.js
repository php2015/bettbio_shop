(function($, window) {
	$.app = {
		init: function() {
			this.initAjax();
		},
		message: function(message,type) {
			var icons = {
				"i": "fa-info-circle",
				"w": "fa-warning"
			};
			var bgs = {
				"i": "success",
				"w": "danger"
			};
			var defaults = {
				type: "i",
				message: "操作成功",
			}
			var template =
				'<div class="alert alert-{bg} alert-dismissible message" role="alert">' +
				'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
				'<ul class="fa-ul"><li><i class="fa {icon} fa-lg fa-li" ></i>{message}</li></ul>' +
				'</div>';
			var options = $.extend({}, defaults, {
				"type": type,
				"message": message
			});

			var alertDom =
				$(template
					.replace("{bg}", bgs[options.type=="i"?"i":"w"])
					.replace("{icon}", icons[options.type=="i"?"i":"w"])
					.replace("{message}", options.message));
			alertDom.appendTo('body');
			var alert = alertDom.alert();
			alert.on('closed.bs.alert', function() {
				alert.remove();
				return true;
			});
			setTimeout(function() {
				alert.remove();
			}, 1000)
		},
		alert: function(options) {
			if(!options) {
				options = {};
			}
			var defaults = {
				title: "警告",
				message: "非法的操作",
				okTitle: "关闭",
				ok: $.noop
			};
			options.alert = true;
			options = $.extend({}, defaults, options);
			this.confirm(options);
		},
		confirm: function(options) {
			var defaults = {
				title: "确认执行操作",
				message: "确认执行操作吗？",
				cancelTitle: '取消',
				okTitle: '确定',
				cancel: $.noop,
				ok: $.noop,
				alert: false
			};

			if(!options) {
				options = {};
			}
			options = $.extend({}, defaults, options);

			var template =
				'<div class="modal fade" data-backdrop="static">' +
				'<div class="modal-dialog">' +
				'<div class="modal-content">' +
				'<div class="modal-header">' +
				' <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
				'  <h4 class="modal-title">{title}</h4>' +
				'</div>' +
				'<div class="modal-body">' +
				'  <p>{message}</p>' +
				'</div>' +
				'<div class="modal-footer">' +
				'<a href="#" class="btn btn-ok btn-danger" data-dismiss="modal">{okTitle}</a>' +
				'<a href="#" class="btn btn-cancel btn-default" data-dismiss="modal">{cancelTitle}</a>' +
				'   </div>' +
				'  </div>' +
				' </div>' +
				'</div>';

			var modalDom =
				$(template
					.replace("{title}", options.title)
					.replace("{message}", options.message)
					.replace("{cancelTitle}", options.cancelTitle)
					.replace("{okTitle}", options.okTitle));

			var hasBtnClick = false;
			if(options.alert) {
				modalDom.find(".modal-footer > .btn-cancel").remove();
			} else {
				modalDom.find(".modal-footer > .btn-cancel").click(function() {
					hasBtnClick = true;
					options.cancel();
				});
			}
			modalDom.find(".modal-footer > .btn-ok").click(function() {
				hasBtnClick = true;
				options.ok();
			});

			var modal = modalDom.modal();

			modal.on("hidden.bs.modal", function() {
				modal.remove(); //直接移除
				if(hasBtnClick) {
					return true;
				}
				if(options.alert) {
					options.ok();
				} else {
					options.cancel();
				}
			});

			return modal;
		},
		dialog: function(title, url, options) {
			var defaults = {
				title: title,
				url: url,
				scroll: true,
				lg: true 
			}
			if(!options) {
				options = {};
			}
			options = $.extend({}, defaults, options);

			var template =
				'<div class="modal fade" data-backdrop="static">' +
				'<div class="modal-dialog {lg}">' +
				'<div class="modal-content">' +
				'<div class="modal-header">' +
				' <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
				'  <h4 class="modal-title">{title}</h4>' +
				'</div>' +
				'<div class="modal-body">' +
				'<iframe src="{url}" width="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>' +
				'</div>' +
				'  </div>' +
				' </div>' +
				'</div>';

			var dialogDom =
				$(template
					.replace("{title}", options.title)
					.replace("{url}", options.url)
					.replace("{lg}",options.lg?'modal-lg':''));

			/*$.ajax({
				url: url,
				cache: false,
				dataType: "html",
				success: function(data) {
					dialogDom.find(".modal-body").html(data);
				}
			})*/

			if(options.scroll) {
				dialogDom.find("iframe").css({
					"height": $(window).height()-150
				})
			} else {
				dialogDom.find("iframe").load(function() {
					//适配iframe内容高度
				})
			}

			var dialog = dialogDom.modal();

			dialog.on("hidden.bs.modal", function() {
				dialog.remove(); //直接移除
			});

			return dialog;
		},
		initDatetimePicker: function() {
			$('.date').each(function() {
				var $date = $(this);
				$date.datetimepicker({
					autoclose: true,
					todayBtn: true,
					pickerPosition: "bottom-left",
					language: "zh-CN"
				});
			});
			$('.datepicker').datetimepicker({
				language: "zh-CN",
				autoclose: true
			});
		},
		initDropify: function() {
			window.drEvent = $('.dropify').dropify();
			drEvent.on('dropify.beforeClear', function(event, element) {
				return confirm("确定要删除  \"" + element.file.name + "\" ?");
			});
			return drEvent;
		},
		initKindEditor: function() {
			window.KEeditor = KindEditor.create('textarea#KE', {
				themeType: 'simple',
				uploadJson: '${ctxPath}/kindeditor/upload',
				fileManagerJson: '${ctxPath}/kindeditor/filemanager',
				allowFileManager: true,
				afterBlur: function() {
					this.sync();
				},
			});
		},
		initAjax:function(){
			$(document).ajaxStart(function() {
				$.blockUI({
					message: ""
				})
			}).ajaxStop($.unblockUI);
		},
		ajaxDefaults:function(){
			return {
					type:"GET",
					data:"",
					async:true,
					dataType:"json",
					success:$.noop,
					error:$.noop
			};
		},
		get:function(url,data,successCbk,errorCbk,async,dataType){
			var options = $.extend({}, this.ajaxDefaults(), {
				type:"GET",
				url:url,
				data:data,
				async:async,
				success:successCbk,
				error:errorCbk,
				dataType:dataType
			});
			this.ajax(options);
		},
		post:function(url,data,successCbk,errorCbk,async,dataType){
			var options = $.extend({}, this.ajaxDefaults(), {
				type:"POST",
				url:url,
				data:data,
				async:async,
				success:successCbk,
				error:errorCbk,
				dataType:dataType
			});
			this.ajax(options);
		},
		ajax:function(options){
			$.ajax({
				type:options.type,
				url:options.url,
				data:options.data,
				async:options.async,
				dataType:options.dataType,
				success:function(data){
					if(data.success==false) $.app.message(data.message);
					if(options.success) {
						options.success(data.success==true,data)
					}
				},
				error:function(data){
					if(data.success==false) $.app.message(data.message);
					if(options.error) options.error(data.success==true,data);
				}
		   });
		},
		printStar : function(n){
			var text = "";
			for(var i = 0; i < n; i++){
				text +='<span class="fa fa-star star-red"></span>';
			}
			return text;
		},
		resetForm : function(_form){
			$(':input',_form)
			.not(':button, :submit, :reset, :hidden')
			 .val('')
			 .removeAttr('checked')
			 .removeAttr('selected'); 
		},
		isEmpty : function(val){
			return val != "undefined" && $.trim(val) != "";
		},
		location:function(_href){
			window.location.href = _href; 
		},
		countDown :function(_this){
			_this.addClass("disabled");
			var index=30;
			var inr = setInterval(function(){
				index--;
				_this.text(index+"后重新获取");
				if(index<0){
					_this.removeClass("disabled").text("获取验证码");
					clearInterval(inr);
				}
			},1000);
		},
		formatCurrencyTenThou:function (s,n) {
			//n = n > 0 && n <= 20 ? n : 2;
			n = n || 1;
			s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
			var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1]; 
			t = ""; 
			for (i = 0; i < l.length; i++) { 
				t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
			} 
			return t.split("").reverse().join("") + "." + r; 
		},
		serializeMap : function (map){
			var arrs = [];
			for(var key in map){
				arrs.push(key+"="+map[key]);
			}
			return arrs.join("&");
		},
		isImage : function(filename) {
	        return /gif|jpe?g|png|bmp$/i.test(filename);
		},
		paramMap : {}
	};
	$(document).ready(function() {
		$('[data-toggle="dialog"]').click(function(e) {
			e.preventDefault();
			var $this = $(this);
			
			$.app.dialog($this.data("title"), $this.attr("href") || $this.data("target"),{
				lg: $this.data('lg')==1 ? false : true
			});
		})
		$('[data-select="all"]').change(function() {
			if (this.checked) $(this).check();
			else $(this).uncheck(); 
		})
		$.app.init();
	});
	(function(){
		var rs = window.location.search.split('?').slice(1);
		var map = {};
		var arrs = rs.toString().split('&');
		for ( var i in arrs) {
			var arr = arrs[i].toString().split('=');
			map[arr[0]] = arr[1];
		}
		$.app.paramMap = map;
	})()
})(jQuery, this);

(function($, window) {
	$.fn.extend({
		check: function() {
			this.checked = true;
			return $(":checkbox[name=" + $(this).val() + "]").each(function() {
				this.checked = true;
			});
		},
		uncheck: function() {
			this.checked = false;
			return $(":checkbox[name=" + $(this).val() + "]").each(function() {
				this.checked = false;
			});
		}
	});
})(jQuery, this);

(function($, window, undefined) {
	// outside the scope of the jQuery plugin to
	// keep track of all dropdowns
	var $allDropdowns = $();

	// if instantlyCloseOthers is true, then it will instantly
	// shut other nav items when a new one is hovered over
	$.fn.dropdownHover = function(options) {

		// the element we really care about
		// is the dropdown-toggle's parent
		$allDropdowns = $allDropdowns.add(this.parent());

		return this.each(function() {
			var $this = $(this).parent(),
				defaults = {
					delay: 100,
					instantlyCloseOthers: true
				},
				data = {
					delay: $(this).data('delay'),
					instantlyCloseOthers: $(this).data('close-others')
				},
				settings = $.extend(true, {}, defaults, options, data),
				timeout;

			$this.hover(function() {
				if(settings.instantlyCloseOthers === true)
					$allDropdowns.removeClass('open');

				window.clearTimeout(timeout);
				$(this).addClass('open');
			}, function() {
				timeout = window.setTimeout(function() {
					$this.removeClass('open');
				}, settings.delay);
			});
		});
	};

	// apply dropdownHover to all elements with the data-hover="dropdown" attribute
	$(document).ready(function() {
		$('[data-hover="dropdown"]').dropdownHover();
	});
})(jQuery, this);
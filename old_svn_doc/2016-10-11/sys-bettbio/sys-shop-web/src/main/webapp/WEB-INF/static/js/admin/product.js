$(function(){
	$("#ztControl :input").click(function() {
		$("#ztControl .ztree-hd").show()
	}).blur(function() {

	});
	
	$('.upload-hd input[type="file"]').change(function(){
		$(this).parent().addClass('has-preview')
	})
	$('.upload-hd .upload-delete').click(function(){
		$(this).parent().removeClass('has-preview');
		$(this).parent().find("input").removeAttr("url");
	})
})

var setting;
		var zTree;
		var treeIds={};
		function loadTree(zNodes){
			 setting = {
						edit: {
							enable: true,
							showRemoveBtn: false,
							showRenameBtn: false,
							drag: {
								isMove: true,
								prev: true,
								inner: true,
								next: true
							}
						},
						callback: {
							onExpand:zTreeOnExpand,
							onClick:checkClass
						},
					};
			zTree = $.fn.zTree.init($("#ZT"), setting, zNodes);
		}
		
		function checkClass(event, treeId, treeNode){
			$("#productClassName").val(treeNode.name);
			$("#productClassCode").val(treeNode.id);
			$("#ztControl .ztree-hd").hide();
			verdict.sucInput($("#productClassName"));
		}
		
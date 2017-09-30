$(function(){
	$("#ztControl :input").click(function() {
		$("#ztControl .ztree-hd").show()
	}).blur(function() {

	});
	
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
			$("#ztControl .ztree-hd").hide()
		}
		
		function showCreateVoucher(){
			$("#createVoucherDiv").show();
      		$("#voucherListDiv").hide();
		}
		function backVoucherList(){
			$("#createVoucherDiv").hide();
      		$("#voucherListDiv").show();
		}
		function showCreateAttestation(){
			$("#attestationListDiv").hide();
      		$("#createAttestationDiv").show();
		}
		function backAttestationList(){
			$("#attestationListDiv").show();
      		$("#createAttestationDiv").hide();
		}
		function showCreatePrice(){
			$("#priceListDiv").hide();
      		$("#createPriceDiv").show();
		}
		function backCreatePrice(){
			$("#priceListDiv").show();
      		$("#createPriceDiv").hide();
		}
		
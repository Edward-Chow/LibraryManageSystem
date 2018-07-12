
function getReaderInfo(id){

	ajax(
			  {
			  	method:'POST',
	    		url:'admin/readerManageAction_getReader.action',
				params: "readerId=" + id,
				type:"json",
	    		callback:function(data) {
					$("#findPaperNO").val(data.cardId);
					$("#findReaderName").val(data.name);
					$("#findEmail").val(data.email);
					$("#findPhone").val(data.phone);
					$("#findReaderType").val(data.readerType.readerTypeName);
					$("#findAdmin").val(data.admin.name);
				}
			}
   
	);
}
				
			
				
				
		   
	
	
			






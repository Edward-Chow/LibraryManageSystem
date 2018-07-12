window.onload = new function() {
    ajax(
        {
            url:"bookManageAction_getAllBookTypes.action",
            type:"json",
            callback:function(data) {
                // 循环遍历每个图书分类，每个名称生成一个option对象，添加到<select>中
                for (var index in data) {
                    //创建一个指名名称元素
                    var option = document.createElement("option");
                    //设置op的实际值为当前的图书分类编号
                    option.value = data[index].typeId;
                    //创建文本节点
                    var textNode = document.createTextNode(data[index].typeName);
                    //把文本子节点添加到op元素中，指定其显示值
                    option.appendChild(textNode);
                    document.getElementById("bookTypeId").appendChild(option);
                }
            }
        }
    );
};
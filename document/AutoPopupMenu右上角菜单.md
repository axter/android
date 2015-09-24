## AutoPopupMenu右上角菜单
> 初始化

```java
popupMenu = new AutoPopupMenu(context, R.layout.shanpaogroup_popub_groupmenu);
//监听控件
{
	View view = popupMenu.getView();
	view.findViewById(R.id.group_menu_search).setOnClickListener(this);
	view.findViewById(R.id.group_menu_create).setOnClickListener(this);
}
//设置是否显示指标
popupMenu.setPoint(R.id.img_point);
```

> 显示关闭

```java
//关闭,一般放在OnPause()方法内
popupMenu.dismiss();

//显示
popupMenu.showAsDropDown(right_res);
```
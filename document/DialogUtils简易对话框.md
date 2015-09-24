## DialogUtils简易对话框

```java
if(dialog == null){
	dialog = DialogUtils.getCenterDialog(context, R.layout.shanpaogroup_dialog_ok);
	TextView tv_content = (TextView)dialog.findViewById(R.id.tv_content);
	Button btn_create = (Button)dialog.findViewById(R.id.dialog_btn_ok);
	tv_content.setText(R.string.group_modify_dialog_waitverify);
	btn_create.setOnClickListener(ShanPaoGroupModifyGroupActivity.this);
}
dialog.show();
```
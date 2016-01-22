（一）、调用本地联系人： 
 Intent intent = new Intent(Intent.ACTION_PICK); 
 intent.setType(ContactsContract.Contacts.CONTENT_TYPE); 
 startActivityForResult(intent, PICK_CONTACT); 
（二）、调用图库，获取所有本地图片： 
Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT); 
 imageIntent.setType("image/*"); 
 startActivityForResult(imageIntent, PICK_IMAGE); 
（三）、调用音乐，获取所有本地音乐文件： 
Intent audioIntent = new Intent(Intent.ACTION_GET_CONTENT); 
audioIntent.setType("audio/*"); 
startActivityForResult(audioIntent, PICK_AUDIO); 
（四）、调用图库，获取所有本地视频文件： 
Intent videoIntent = new Intent(Intent.ACTION_GET_CONTENT); 
videoIntent.setType("video/*"); 
startActivityForResult(videoIntent, PICK_VIDEO); 
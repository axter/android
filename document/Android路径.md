### Android内置路径获取
```java
///system
System.out.println(Environment.getRootDirectory());
///cache
System.out.println(Environment.getDownloadCacheDirectory());
///storage/emulated/0
System.out.println(Environment.getExternalStorageDirectory());
///data
System.out.println(Environment.getDataDirectory());

///data/data/com.example.testdir/files
System.out.println(getApplicationContext().getFilesDir());
///data/data/com.example.testdir/cache
System.out.println(getApplicationContext().getCacheDir());
///data/data/com.example.testdir/databases/bc
System.out.println(getApplicationContext().getDatabasePath("bc"));


///data/app/com.example.testdir-1.apk
System.out.println(getApplicationContext().getPackageCodePath());
System.out.println(getApplicationContext().getPackageResourcePath());
///data/data/com.example.testdir/files/cd
System.out.println(getApplicationContext().getFileStreamPath("cd"));

///storage/emulated/0/Android/obb/com.example.testdir
System.out.println(getApplicationContext().getObbDir());
///storage/emulated/0/Android/data/com.example.testdir/files/db
System.out.println(getApplicationContext().getExternalFilesDir("db"));
///storage/emulated/0/Android/data/com.example.testdir/cache
System.out.println(getApplicationContext().getExternalCacheDirs());
```

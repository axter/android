### 第一步,添加依赖
```xml
dependencies {
	compile 'com.android.support:multidex:1.0.1'
}
```

### 第二步,开启multiDexEnabled
```xml
defaultConfig {
	multiDexEnabled true
}
```

### 第三部,添加代码支持
> 方法1,继承android.support.multidex.MultiDexApplication
> 方法2,重写 attachBaseContext()方法 , 增加 MultiDex.install(this);

### 可能出现Out of memory问题
```xml
dexOptions {
	incremental true javaMaxHeapSize "4g"
}
```


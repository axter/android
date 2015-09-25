## 本地目录自动生成工程打包文件
```cmd
android update project -p . -t android-22
```

## 设置ant.properties
```pro
key.store=doc/xxx.keystore
key.store.password=xxx
key.alias=stay
key.alias.password=xxx
```

## 自定义执行 custom_rules.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="custom_rules" >

</project>
```


## 循环功能使用
```xml
<!-- 申明循环功能,判断语句就会有问题 -->
<taskdef resource="net/sf/antcontrib/antcontrib.properties">  
    <classpath>  
        <pathelement location="lib/ant-contrib-1.0b3.jar"/>  
    </classpath>  
</taskdef>  
<!-- 循环,遍历market_channels变量,分割,保存至channel变量内 -->
<target name="deploy">  
   <foreach target="modify_manifest" list="${market_channels}" param="channel" delimiter="," />       
</target>  

<target name="modify_manifest">  
    <replaceregexp flags="g" byline="false">  
        <!-- 匹配的内容是 android:value="*****" android:name="UMENG_CHANNEL" -->  
        <regexp pattern='android:value="(.*)" android:name="UMENG_CHANNEL"' />  
        <!-- 匹配之后将其替换为 android:value="渠道名" android:name="UMENG_CHANNEL" -->  
        <substitution expression='android:value="${channel}" android:name="UMENG_CHANNEL"' /> 
        <!-- 正则表达式需要匹配的文件为AndroidManifest.xml -->  
        <fileset dir="" includes="AndroidManifest.xml" />  
    </replaceregexp>
    <property name="out.release.file" location="${out.absolute.dir}/${ant.project.name}_${channel}.apk" />  
     <!--包 -->  
     <antcall target="release" />  
     <!--输出渠道包到bin/out目录下 -->  
    <copy tofile="${out.absolute.dir}/out/${ant.project.name}v${version}-${channel}.apk" file="bin/${ant.project.name}-release.apk"/>  
</target>
```


## 一份完整的打包代码
```xml
<!-- 打包脚本  ant deploy-->    
<target name="deploy">
   <!-- 清理目录 -->
   <antcall target="clean" />
   <!-- 读取工程属性 -->
   <antcall target="shanpao_info" />
   <!-- 读取配置文件至内存 -->
   <property file="bin/auto.prop" />
   <!-- 备份文件 -->
   <antcall target="shanpao_bak"/>
   <!-- 测试包 -->
   <antcall target="shanpao_debug" />
   <!-- 正式包 -->
   <antcall target="shanpao_release" />
   <!-- 恢复修改的文件 -->
   <antcall target="shanpao_recovery" />
</target>

<!-- 解析xml文件 -->
<target name="shanpao_info">
    <!-- 解析AndroidManifest.xml 获得包名 -->
    <xmlproperty file="AndroidManifest.xml" collapseAttributes="true" />
    <!-- 创建目录,目录不存在文件创建不了 -->
    <mkdir dir="bin/"/>
    <!-- 写入配置文件 -->
    <propertyfile file="bin/auto.prop">
        <entry key="auto.package" value="${manifest.package}" />
        <entry key="auto.final.versionCode" value="${manifest.android:versionCode}" />
        <entry key="auto.final.versionName" value="${manifest.android:versionName}" />
    </propertyfile>
</target>

<target name="shanpao_print">
	<!-- 打印属性 -->
    <echo message="version: ${auto.final.versionName}" />
</target>

<target name="shanpao_debug">
	<!-- 匹配内容,改写内容 -->
    <replaceregexp encoding="UTF-8" flags="g" byline="false">  
        <regexp pattern='public static final boolean AILI_TEST = (.*);' />
        <substitution expression='public static final boolean AILI_TEST = true;' /> 
        <fileset dir="src/com/axter/shanpao" includes="AppConfig.java" />
    </replaceregexp>
    
    <replaceregexp encoding="UTF-8" flags="h" byline="false">
        <regexp pattern='android:name="com.amap.api.v2.apikey" android:value="(.*)"' />
        <substitution expression='android:name="com.amap.api.v2.apikey" android:value="98f43e20201147ac6e929e019e1d805e"' /> 
        <fileset dir="" includes="AndroidManifest.xml" />
    </replaceregexp>
    
	<replaceregexp encoding="UTF-8" flags="i" byline="false">
        <regexp pattern='android:name="RONG_CLOUD_APP_KEY" android:value="(.*)"' />
        <substitution expression='android:name="RONG_CLOUD_APP_KEY" android:value="pkfcgjstfqqr8"' /> 
        <fileset dir="" includes="AndroidManifest.xml" />
    </replaceregexp>
	<!-- 编译正式包 -->
    <antcall target="release" />
    <!-- 拷贝文件到相应目录下 -->
    <copy tofile="${out.absolute.dir}/out/${ant.project.name}_v${auto.final.versionName}-test.apk" file="bin/${ant.project.name}-release.apk"/>
 </target>


<target name="shanpao_release">
    <replaceregexp encoding="UTF-8" flags="j" byline="false">  
        <regexp pattern='public static final boolean AILI_TEST = (.*);' />
        <substitution expression='public static final boolean AILI_TEST = false;' /> 
        <fileset dir="src/com/axter/shanpao" includes="AppConfig.java" />
    </replaceregexp>
    
    <replaceregexp encoding="UTF-8" flags="k" byline="false">
        <regexp pattern='android:name="com.amap.api.v2.apikey" android:value="(.*)"' />
        <substitution expression='android:name="com.amap.api.v2.apikey" android:value="98f43e20201147ac6e929e019e1d805e"' /> 
        <fileset dir="" includes="AndroidManifest.xml" />
    </replaceregexp>
    
	<replaceregexp encoding="UTF-8" flags="l" byline="false">
        <regexp pattern='android:name="RONG_CLOUD_APP_KEY" android:value="(.*)"' />
        <substitution expression='android:name="RONG_CLOUD_APP_KEY" android:value="qd46yzrf42mlf"' /> 
        <fileset dir="" includes="AndroidManifest.xml" />
    </replaceregexp>

	<antcall target="release" />
    <copy tofile="${out.absolute.dir}/out/${ant.project.name}_v${auto.final.versionName}-release.apk" file="bin/${ant.project.name}-release.apk"/>
</target>

<target name="shanpao_bak">
	<!-- 拷贝,备份文件 -->
    <copy file="src/com/axter/shanpao/AppConfig.java" tofile="bin/AppConfig.java.bak"/>
    <copy file="AndroidManifest.xml" tofile="bin/AndroidManifest.xml.bak"/>
</target>

<target name="shanpao_recovery">
	<!-- 先删除才能覆盖 -->
    <delete file="src/com/axter/shanpao/AppConfig.java"/>
    <delete file="AndroidManifest.xml"/>
    <copy file="bin/AppConfig.java.bak" tofile="src/com/axter/shanpao/AppConfig.java"/>
    <copy file="bin/AndroidManifest.xml.bak" tofile="AndroidManifest.xml"/>
</target>
```





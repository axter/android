# React Native开发

> http://facebook.github.io/react-native/docs/getting-started.html

## 配置需求
	Node.js 4.0以上版本

## 快速开始
```cmd
$ npm install -g react-native-cli
$ react-native init AwesomeProject
```
## 运行Android版本
```cmd
$ cd AwesomeProject
$ react-native run-android
```

## 测试情况
打开 index.android.js 修改几行.
执行 ==adb logcat *:S ReactNative:V ReactNativeJS:V== 查看log

## 问题
Starting the packager in a new window is not supported on Windows yet.
需要手动启动

```cmd
$ react-native start
```

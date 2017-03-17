# RxGalleryFinal

## 功能描述

   RxGalleryFinal是一个android图片/视频文件选择器。其支持多选、单选、拍摄和裁剪，主题可自定义，无强制绑定第三方图片加载器。

### 新版本 V0.08

    1.增加日志输出的debug功能，防止多层DebugConfig的问题
    2.去掉Toast，图片多选给到自定义的事件
    3.解决7.0奔溃的问题
  
### 配置manifest

* 添加权限
  
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```
* 注册activity
  
```xml
<application
    ...
    android:theme="@style/Theme_Light">
<activity
    android:name="cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity"
    android:screenOrientation="portrait"
    android:exported="true"
    android:theme="@style/Theme_Light.Default"/>
<activity
    android:name="com.yalantis.ucrop.UCropActivity"
    android:screenOrientation="portrait"
    android:theme="@style/Theme_Light.Default"/>
</application
```
这里可以配置主题
* 打开图片浏览器

```java
RxGalleryFinal
.with(context)
.image()
.radio()
.crop()
.imageLoader(ImageLoaderType.GLIDE)
.subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
    @Override
    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
        //图片选择结果
        .....
    }
})
.openGallery();
```
----
<a href="https://github.com/FinalTeam/RxGalleryFinal/blob/master/sample/src/main/java/cn/finalteam/rxgalleryfinal/sample/MainActivity.java" targer="_blank">Sample 代码</a>


```java
 //得到图片多选的事件
        RxGalleryListener.getInstance().setMultiImageCheckedListener(new IMultiImageCheckedListener() {
                 @Override
                 public void selectedImg(Object t, boolean isChecked) {
                   //这个主要点击或者按到就会触发，所以不建议在这里进行Toast
                    /*  Toast.makeText(getBaseContext(),"->"+isChecked,Toast.LENGTH_SHORT).show();*/
                 }
                 @Override
                 public void selectedImgMax(Object t, boolean isChecked, int maxSize) {
                    Toast.makeText(getBaseContext(), "你最多只能选择" + maxSize + "张图片", Toast.LENGTH_SHORT).show();
                 }
        });
```

<hr/>
```java
  //手动打开日志。
        ModelUtils.setDebugModel(true);
```

 截图：

![image](https://github.com/FinalTeam/RxGalleryFinal/blob/master/a1.png)


## 使用
### 下载或添加依赖
  在module gradle中项目依赖代码：

  ```gradle
    compile 'cn.finalteam.rxgalleryfinal:library:0.0.8'
    //rxgalleryfinal依赖appcompat-v7和recyclerview-v7扩展卡库
    compile 'com.android.support:recyclerview-v7:24.2.0'
    compile 'com.android.support:appcompat-v7:24.2.0'

    //支持以下主流图片加载器，开发者自行选择
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'com.facebook.fresco:fresco:0.12.0'
    compile 'com.github.bumptech.glide:glide:3.7.0'
    compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
  ```

## 混淆配置
```xml
#1.support-v7-appcompat
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

#2.rxjava
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#3.retrolambda
-dontwarn java.lang.invoke.*

#4.support-v4
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

#5.ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#6.photoview
-keep class uk.co.senab.photoview** { *; }
-keep interface uk.co.senab.photoview** { *; }

#7.rxgalleryfinal
-keep class cn.finalteam.rxgalleryfinal.ui.widget** { *; }

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepattributes *Annotation*
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
```

## Q&A
* 1、出现图片倒立问题，如何解决
* 2、如何压缩图片
* 3、Android 7.0闪退

## 联系
    如果有紧急事件可联系作者或加Q群：
    - Q群号： 218801658
    - Q群号： 246231638




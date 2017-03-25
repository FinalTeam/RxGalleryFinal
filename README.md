# RxGalleryFinal

## 功能描述（JDK1.8）

   RxGalleryFinal是一个android图片/视频文件选择器。其支持多选、单选、拍摄和裁剪，主题可自定义，无强制绑定第三方图片加载器。

### 待完善

    1.视频选择器的回调

### 新版本 V 0.0.9

    -- compile 'cn.finalteam.rxgalleryfinal:library:0.0.9'
    1.增加直接打开相机的功能
    2.去掉 char[] java.lang.String.toCharArray()

### 新版本 V 0.0.8

    -- compile 'cn.finalteam.rxgalleryfinal:library:0.0.8'
    1.增加日志输出的debug功能，防止多层DebugConfig的问题
    2.去掉Toast，图片多选给到自定义的事件
    3.解决7.0奔溃的问题
    4.去掉 相关NULL引起的问题

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


### 配置manifest

 截图：

![image](https://github.com/FinalTeam/RxGalleryFinal/blob/master/a1.png)

* 提供了相关的Api

* 请查看MainActivity的示例代码

```java
        //调用图片选择器Api
        initClickSelImgListener();//三选一
        //调用视频选择器Api
        initClickSelVDListener();//三选一
```


<a href="https://github.com/FinalTeam/RxGalleryFinal/blob/master/sample/src/main/java/cn/finalteam/rxgalleryfinal/sample/MainActivity.java" targer="_blank"> 查看 Sample 代码</a>

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

![image](https://github.com/FinalTeam/RxGalleryFinal/blob/master/device-2017-03-24-181216.png =700x700)

* 打开图片浏览器

```java
//自定义单选
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

```java
   //自定义多选
  RxGalleryFinal
                    .with(MainActivity.this)
                    .image()
                    .multiple()
                    .maxSize(8)
                    .imageLoader(ImageLoaderType.UNIVERSAL)
                    .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {

                        @Override
                        protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                            Toast.makeText(getBaseContext(), "已选择" + imageMultipleResultEvent.getResult().size() + "张图片", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            Toast.makeText(getBaseContext(), "OVER", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .openGallery();

```

---


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
----

```java
//注解诠释
 RxGalleryFinal.with(context)
                    .image()//图片
                    .radio()//单选
                    .crop()//裁剪
                    .video()//视频
                    .imageLoader(ImageLoaderType.GLIDE)//里面可以选择主流图片工具  PICASSO  GLIDE  FRESCO UNIVERSAL(ImageLoader)
                    .subscribe(rxBusResultSubscriber)
                    .openGallery();
```

----

```java

  //手动打开日志。
        ModelUtils.setDebugModel(true);

```
----


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




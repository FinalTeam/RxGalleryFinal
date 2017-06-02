# RxGalleryFinal


## Functional description（JDK1.8）

   RxGalleryFinal is an android image/video file selector.Its support for multiple, radio, film and cutting, the theme can be custom, no mandatory binding third-party image loader.

   * <a href="https://github.com/FinalTeam/RxGalleryFinal/blob/master/README.md" target="_blank"> Chinese Version description </a>
   * <a href="https://github.com/FinalTeam/RxGalleryFinal/blob/master/README_English.md" target="_blank"> The English description </a>


## Version described

 <a href="https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-Issues" target="_blank">History Issues</a> <br/>
 <a href="https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-Version" target="_blank">History Version</a>

### To be perfect

    1.Video of the callback
    2.RxJAVA upgrade
    3.Caton test, search in Issues: [fine] feel the point of Caton, I #130


   ### 新版本 V 1.0.7
        -- compile 'cn.finalteam.rxgalleryfinal:library:1.0.7'
        1.RxJava Update to - RxJava2.1
        2.Repair bug ->  #136,#135,#134,#129,#99

    ### New version 1.0.6
         -- compile 'cn.finalteam.rxgalleryfinal:library:1.0.6'
         1. Repair click Home, and then return to the interface, the picture will increase the problem.
         2. Repair UI preview size for zero nums problems.
         3. Repair the onresume () life cycle and invoke the onScanCompleted problem.
         4. Repair cuts(crop), callbacks, and picture MediaActivity closure issues.
         5. Repair Some models Caton problems.

    ### New version 1.0.5
        -- compile 'cn.finalteam.rxgalleryfinal:library:1.0.5'
        1.Repair 1.0.3 Bug，picasso preview big images of the crash. #119
        2.Provide the callback after cutting onCropImageResult() ， For close selection interface
        3.Api Relevant methods to optimize
        4.Optimization related to Null pointer

   ### New version 1.0.4
        -- compile 'cn.finalteam.rxgalleryfinal:library:1.0.4'
        1.Repair 1.0.3 Bug，picasso preview big images of the crash. #119

   ### New version 1.0.3
        -- the compile 'cn. Finalteam. Rxgalleryfinal: library: 1.0.3'
        1. Repair the custom tailoring proportion function failure # 114
        2. Repair cannot cut PNG format images?# 77
        3. Repair alternative picture more than the maximum null pointer # 107
        4. Repair the radio photos exist null pointer # 105
        5. Adding custom tailoring proportion method and Api # 115
        6. Picasso was the original image scaling problems
        7. Multiple calls in the demo commented examples, according to their own needs to choose the corresponding API
        8. According to the requirements of all - cutting when the callback onEvent selection events


    ### New version 1.0.2
         -- the compile 'cn. Finalteam. Rxgalleryfinal: library: 1.0.2'
         1. Solve the cutting picture "only have a problem", without setting can cut many times
         2. Increase the callback cut out of the picture
         3. Increase the preservation and the cutting path can be set up, is not set automatically stored in the default path
         4. Solve the problem of a dark figure
         5. Configure the topic - gallery_attrs. XML/gallery_default_theme XML

    ### New version 1.0.1
         -- the compile 'cn. Finalteam. Rxgalleryfinal: library: 1.0.1'
         1. Upgrade UCrop tailoring library - 2.2.0
         2. Increase the Api directly open cut
         3. Keep ucrop on engineering, not references.If there is a need to local lib can combine their own needs

    ###  New version 1.0.0
        -- the compile 'cn. Finalteam. Rxgalleryfinal: library: 1.0.0'
        1. Test version

    ### New version V 0.0.9
        -- the compile 'cn. Finalteam. Rxgalleryfinal: library: 0.0.9'
        1. Increase directly open the camera function
        2. Remove the char [] Java. Lang. String. ToCharArray ()

    ### New version V 0.0.8
        -- the compile 'cn. Finalteam. Rxgalleryfinal: library: 0.0.8'
        1. Increase the debug log output function, prevent multilayer DebugConfig problem
        2. Remove the Toast, image pops up to custom events
        3. Solve the problem of 7.0 collapses
        4. Remove the relevant problems caused by NULL


## Use
### Download or add a dependency
   In the module of gradle depends on project code:

  ```gradle
    compile 'cn.finalteam.rxgalleryfinal:library:1.0.1'

    //Rxgalleryfinal rely on appcompat - v7 and recyclerview - v7 expansion card library
    compile 'com.android.support:recyclerview-v7:24.2.0'
    compile 'com.android.support:appcompat-v7:24.2.0'

    //Supports the following image loader, mainstream developers to choose
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'com.facebook.fresco:fresco:0.12.0'
    compile 'com.github.bumptech.glide:glide:3.7.0'
    compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
  ```


### Configuration Manifest

 screenshots：

![image](https://github.com/FinalTeam/RxGalleryFinal/blob/master/a1.png)


* Provide the related Api

* Please check the MainActivity sample code : <a href="https://github.com/FinalTeam/RxGalleryFinal/blob/master/sample/src/main/java/cn/finalteam/rxgalleryfinal/sample/MainActivity.java" targer="_blank"> 查看 Sample 代码</a>


```java
   //The use of custom methods
   onClickZDListener();
   //Api call image selectors
   onClickSelImgListener();
   //Api call video selectors
   onClickSelVDListener();
   //Call cutting Api
   onClickImgCropListener();
```

```java

   //Manually open the log.
   ModelUtils.setDebugModel(true);

```

* Here you can configure the theme
    <img src="https://github.com/FinalTeam/RxGalleryFinal/blob/master/device-2017-04-11-154816.png" style="zoom:40%"  width=720 height=1080/>

<hr/>

* Code

```java
//The radio of the custom method
RxGalleryFinal
.with(context)
.image()
.radio()
.crop()
.imageLoader(ImageLoaderType.GLIDE)
.subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
    @Override
    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
        //图片选择结果Image selection results
        .....
    }
})
.openGallery();
```

----

```java
//The custom method multi-select
RxGalleryFinal.with(MainActivity.this)
.image()
.multiple()
.maxSize(8)
.imageLoader(ImageLoaderType.UNIVERSAL)
.subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
       @Override
       protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
          toast("已选择" + imageMultipleResultEvent.getResult().size() + "张图片");
       }
       @Override
       public void onCompleted() {
       super.onCompleted();
           Toast.makeText(getBaseContext(), "OVER", Toast.LENGTH_SHORT).show();
       }
}).openGallery();

```

---


```java
 //Alternative events get pictures
 RxGalleryListener.getInstance().setMultiImageCheckedListener(new IMultiImageCheckedListener() {
       @Override
       public void selectedImg(Object t, boolean isChecked) {
            //这个主要点击或者按到就会触发，所以不建议在这里进行Toast
            //The main click or press that will trigger, so it is not recommended to Toast here
       }
       @Override
       public void selectedImgMax(Object t, boolean isChecked, int maxSize) {
           toast("你最多只能选择" + maxSize + "张图片");
       }
});
```
----

```java
 //Interpretation
 RxGalleryFinal.with(context)
      .image()//图片
      .radio()//单选
      .crop()//裁剪
      .video()//视频
      //Can choose the mainstream pictures inside:   PICASSO  GLIDE  FRESCO UNIVERSAL(ImageLoader)
      .imageLoader(ImageLoaderType.GLIDE)
      .subscribe(rxBusResultSubscriber)
      .openGallery();
```

----
```java
    //调用裁剪.RxGalleryFinalApi.getModelPath()为默认的输出路径
    //The output of the call cut .RxGalleryFinalApi.getModelPath() for the default path
    RxGalleryFinalApi.cropScannerForResult(MainActivity.this, RxGalleryFinalApi.getModelPath(), inputImg);
```
----

```java
    //获取和设置 保存路径:Get and set path
    //By cutting path
    RxGalleryFinalApi.getImgSaveRxCropDirByFile();//得到裁剪路径
    RxGalleryFinalApi.getImgSaveRxCropDirByStr();//得到裁剪路径
    //Get image path
    RxGalleryFinalApi.getImgSaveRxDirByFile();//得到图片路径
    RxGalleryFinalApi.getImgSaveRxCropDirByStr();//得到图片路径

    //Get and set path
    //…… setImgSaveXXXXX().
    //图片自动会存储到下面，裁剪会自动生成路径；也可以手动设置裁剪的路径；
    //Will automatically stored in the pictures below,cut automatically generated path;
    //Can also manually cutting path;
    RxGalleryFinalApi.setImgSaveRxSDCard("dujinyang");
```
----


```java
    //自定义裁剪Custom tailoring
   rx.cropAspectRatioOptions(0, new AspectRatio("3:3",30, 10))
   .crop()
   .openGallery();
```
----

```java
  //4.演示 单选裁剪 并且增加回掉 （裁剪必须在open之前）
  RxGalleryFinalApi.getInstance(this)
     .onCrop(true)//是否裁剪
     .openGalleryRadioImgDefault(new RxBusResultSubscriber() {
             @Override
             protected void onEvent(Object o) throws Exception {
                  Logger.i("只要选择图片就会触发");
             }
      })
     .onCropImageResult(new IRadioImageCheckedListener() {
             @Override
             public void cropAfter(Object t) {
                  Logger.i("裁剪完成");
             }

             @Override
             public boolean isActivityFinish() {
                  Logger.i("返回false不关闭，返回true则为关闭");
                  return true;
             }
     });
```


* Add permissions

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

* Registered activity
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

## Confuse configuration
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
* 1、Pictures on problems, how to solve
* 2、How to compress images
* 3、Android 7.0 Flash back
* 4、Authorized instructions

## Contact
   If there is an emergency can contact the Author or add QQ group:
    - QQ Group： 218801658
    - QQ Group： 246231638

## Wiki

   * <a href="https://github.com/FinalTeam/RxGalleryFinal/wiki/GalleryFinal-%E9%97%AE%E9%A2%98%E7%B3%BB%E5%88%97" targer="_blank"> GalleryFinal Series of problems </a>
   * <a href="https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-%E9%97%AE%E9%A2%98%E7%B3%BB%E5%88%97" targer="_blank"> RxGalleryFinal Series of problems </a>
   * <a href="https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-Version" targer="_blank"> RxGalleryFinal Version </a>




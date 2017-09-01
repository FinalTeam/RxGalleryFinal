# RxGalleryFinal

##  翻訳

  情報 ：KARL-Dujinyang
 <br/> 情報 ： 杜锦阳 - [sheep0704](https://github.com/sheep0704)

## の機能的な記述（JDK1.8）

   rxgalleryfinal android画像/ビデオファイルセレクタ。複数の無線のための支援を、フィルムと切断できるカスタムテーマは、義務的な結合のサードパーティのイメージでない。


   * [English](https://github.com/FinalTeam/RxGalleryFinal/blob/master/README_English.md)
   * [Japanese](https://github.com/FinalTeam/RxGalleryFinal/blob/master/README_Japanese.md)
   * [Chinese](https://github.com/FinalTeam/RxGalleryFinal)

## 版について

[History Issues](https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-Issues)

[History Version](https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-Version)

### To be perfect

    1つのコールバックのビデオ。
    2ケイトン試験においては、問題の探索：細かい点のケイトン感じます、私は130


### gradle

	   New : compile 'cn.finalteam.rxgalleryfinal:library:1.1.3' -> 緊急修理のバグ bug.
	   (Fix #191, add video)

 参考：[History Version](https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-Version)

### 1.1.1 の特性

 * 修復 のバグ bug
 * #170,#165 ,#167 と#修正画像などのファイルまたはディレクトリはありません
 * 最新版のサンプルコード

### 1.0.9 の特性

 * 修復のバグ bug -  #160
 * ucrop設定の追加
 * 最新版のサンプルコード
 * rxjava更新

## 使用
### をダウンロードするか、依存関係を追加

    compile 'cn.finalteam.rxgalleryfinal:library:1.1.2'
    compile 'com.android.support:recyclerview-v7:24.2.0'
    compile 'com.android.support:appcompat-v7:24.2.0'

    //以下のイメージローダをサポートし、主流の開発者を選ぶ
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'com.facebook.fresco:fresco:0.12.0'
    compile 'com.github.bumptech.glide:glide:3.7.0'
    compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'



### 構成を明らかにする

 スクリーンショット：

![image](https://github.com/FinalTeam/RxGalleryFinal/blob/master/screenshots/a1.png)


* 関連のapiを提供する

* MainActivity サンプルコードをチェックしてください :  [Sample code](https://github.com/FinalTeam/RxGalleryFinal/blob/master/sample/src/main/java/cn/finalteam/rxgalleryfinal/sample/MainActivity.java)



		   //またはカスタム手法の利用
		   onClickZDListener();
		   //さんの画像をセレクタapi呼び出し
		   onClickSelImgListener();
		   //さんのビデオセレクタapi呼び出し
		   onClickSelVDListener();
		   //切削apiに電話して
		   onClickImgCropListener();
		   //手動でログインします。
		   ModelUtils.setDebugModel(true);



* ここにあなたがテーマを設定することができます
![image](https://github.com/FinalTeam/RxGalleryFinal/blob/master/screenshots/device-2017-04-11-154816.png)

##  テーマ

   テーマ：サンプル構成物のres -> xml→testtheme ....

* コード


	//ラジオは、カスタムの方法
	RxGalleryFinal
	.with(context)
	.image()
	.radio()
	.crop()
	.imageLoader(ImageLoaderType.GLIDE)
	.subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
	    @Override
	    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
	        //画像選択の結果 Image selection results
	        .....
	    }
	})
	.openGallery();


----


	//カスタム方法マルチセレクト
	RxGalleryFinal.with(MainActivity.this)
	.image()
	.multiple()
	.maxSize(8)
	.imageLoader(ImageLoaderType.UNIVERSAL)
	.subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
	       @Override
	       protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
	          toast("選択済み" + imageMultipleResultEvent.getResult().size() + "張画像");
	       }
	       @Override
	       public void onCompleted() {
	       super.onCompleted();
	           Toast.makeText(getBaseContext(), "OVER", Toast.LENGTH_SHORT).show();
	       }
	}).openGallery();


---



	 //Alternative events get pictures
	 RxGalleryListener.getInstance().setMultiImageCheckedListener(new IMultiImageCheckedListener() {
	       @Override
	       public void selectedImg(Object t, boolean isChecked) {
	            //この主なクリックや押しがトリガーので、提案しないここでトーストでした
	            //The main click or press that will trigger, so it is not recommended to Toast here
	       }
	       @Override
	       public void selectedImgMax(Object t, boolean isChecked, int maxSize) {
	           toast("あなたが一番多いのは選択" + maxSize + "張画像");
	       }
	});

----


	 //Interpretation
	 RxGalleryFinal.with(context)
	      .image()//画像
	      .radio()//ラジオ
	      .crop()//裁断
	      .video()//ビデオ
	      //Can choose the mainstream pictures inside:   PICASSO  GLIDE  FRESCO UNIVERSAL(ImageLoader)
	      .imageLoader(ImageLoaderType.GLIDE)
	      .subscribe(rxBusResultSubscriber)
	      .openGallery();


----

	    //裁断を呼び出す..RxGalleryFinalApi.getModelPath() デフォルトの出力経路
	    //The output of the call cut .RxGalleryFinalApi.getModelPath() for the default path
	    RxGalleryFinalApi.cropScannerForResult(MainActivity.this, RxGalleryFinalApi.getModelPath(), inputImg);

----


	    //セットと設定-保存経路:Get and set path
	    //By cutting path
	    RxGalleryFinalApi.getImgSaveRxCropDirByFile();//裁断経路を得る
	    RxGalleryFinalApi.getImgSaveRxCropDirByStr();//裁断経路を得る
	    //画像のパスを取得する
	    RxGalleryFinalApi.getImgSaveRxDirByFile();//写真を得る
	    RxGalleryFinalApi.getImgSaveRxCropDirByStr();//写真を得る
	
	    //ゲットpath andセット
	    //…… setImgSaveXXXXX().
	    //画像自動会ストレージの下、裁断を自動的に生成パスも手動設定裁断経路、
	    //Will automatically stored in the pictures below,cut automatically generated path;
	    //Can also manually cutting path;
	    RxGalleryFinalApi.setImgSaveRxSDCard("dujinyang");

----


	    //カスタマイズ裁ち Custom tailoring
	   rx.cropAspectRatioOptions(0, new AspectRatio("3:3",30, 10))
	   .crop()
	   .openGallery();

----


	  //4.デモンストレーションラジオ裁断そして増加回落ち（裁断しなければならないオープン前）
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



* Add permissions


		<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
		<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />


* Registered activity

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


## Confuse configuration

	#1.support-v7-appcompat
	-keep public class android.support.v7.widget.** { *; }
	-keep public class android.support.v7.internal.widget.** { *; }
	-keep public class android.support.v7.internal.view.menu.** { *; }
	
	-keep public class * extends android.support.v4.view.ActionProvider {
	    public <init>(android.content.Context);
	}
	
	#2.rxjava
	-dontwarn io.reactivex.**
	-keep io.reactivex.**
	-keepclassmembers class io.reactivex.** { *; }	
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


## Q&A

* 1、問題について絵を解く方法
* 2、画像を圧縮する方法
* 3、Android 7.0 アンドロイド7 .フラッシュバック
* 4、Authorized 認可された命令

## Contact接触

    緊急の著者に連絡してまたはqq群を加えることがあるならば：
    - QQ Group群 ： 218801658
    - QQ Group群 ： 246231638

## Wiki

   * [GalleryFinal Series of problems](https://github.com/FinalTeam/RxGalleryFinal/wiki)
   * [RxGalleryFinal Series of problems](https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-%E9%97%AE%E9%A2%98%E7%B3%BB%E5%88%97)
   * [RxGalleryFinal Version](https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-Version)




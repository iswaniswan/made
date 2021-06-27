# proguard configuration for sqlchipter
#----------------------------- begin -----------------------------------
#
-keep,includedescriptorclasses class net.sqlcipher.** { *; }
-keep,includedescriptorclasses interface net.sqlcipher.** { *; }
#
#------------------------------ end ------------------------------------


# proguard configuration for Gson
#----------------------------- begin -----------------------------------
#
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
#
#------------------------------ end ------------------------------------


# proguard configuration for retrofit2
#----------------------------- begin -----------------------------------
#
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions.**
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
#
#------------------------------ end ------------------------------------


# proguard configuration for Glide
#----------------------------- begin -----------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}
#------------------------------ end ------------------------------------

# proguard configuration for Glide
#----------------------------- begin ----------------------------------
-keep class com.pierfrancescosoffritti.androidyoutubeplayer.*
#------------------------------ end ------------------------------------
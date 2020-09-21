package com.blueMarketing.capsula.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import rx.functions.Action1
import java.io.ByteArrayOutputStream

/**
 * Created by amr on 11/02/18.
 */
object ImageUtil {
    private var encodedImage: String? = null
    @Throws(IllegalArgumentException::class)
    fun convert2Bitmap(base64Str: String): Bitmap {
        val decodedBytes = Base64.decode(
            base64Str.substring(base64Str.indexOf(",") + 1),
            Base64.DEFAULT
        )
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    //    public static String convert2Base64(Bitmap bitmap) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        if (bitmap != null) {
//
//            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
//            Log.d("BITMAP-Size", "" + bitmap.getByteCount());
//            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
//        }
//        return "";
//
//    }
//    public static String convert2Base64(final Bitmap bm) {
//        if (bm == null)
//            return "";
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // Bitmap bm = BitmapFactory.decodeFile(imagePath);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bm.compress(Bitmap.CompressFormat.PNG, 50, baos);
//                byte[] byteArrayImage = baos.toByteArray();
//                encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
//            }
//        }).run();
//
//
//        return resizeBase64Image(encodedImage);
//    }
    fun threadedBase64(bm: Bitmap?, callback: Action1<String?>) {
        if (bm == null) return
        Thread(Runnable {
            // Bitmap bm = BitmapFactory.decodeFile(imagePath);
            val result = convert2Base64(bm)
            Handler(Looper.getMainLooper())
                .post { callback.call(result) }
        }).run()
    }

    fun resizeBase64Image(base64image: String?): String? {
        val encodeByte =
            Base64.decode(base64image!!.toByteArray(), Base64.DEFAULT)
        val options = BitmapFactory.Options()
        options.inPurgeable = true
        var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)
        if (image.height <= 400 && image.width <= 400) {
            return base64image
        }
        image = Bitmap.createScaledBitmap(image, 400, 400, false)
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 70, baos)
        val b = baos.toByteArray()
        System.gc()
        return Base64.encodeToString(b, Base64.NO_WRAP)
    }

    fun convert2Base64(bm: Bitmap?): String? {
        if (bm == null) return ""
        Thread(Runnable {
            // Bitmap bm = BitmapFactory.decodeFile(imagePath);
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.PNG, 50, baos)
            val byteArrayImage = baos.toByteArray()
            encodedImage =
                Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
        }).run()
        return resizeBase64Image(encodedImage)
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

//    fun bitmapToTempFile(bitmap: Bitmap): File? {
//        val file: File
//        try {
//            file = File.createTempFile("id_image", ".png", App.getContext().getCacheDir())
//            val out: OutputStream = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
//            out.flush()
//            out.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            return null
//        }
//        file.deleteOnExit()
//        return file
//    }

    fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver, inImage,
            "Title", null
        )
        return Uri.parse(path)
    }
}
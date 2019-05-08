package com.kuyuzhiqi.bizbase.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions.*
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.util.Util
import com.kuyuzhiqi.bizbase.BizConstant
import com.kuyuzhiqi.bizbase.R
import com.kuyuzhiqi.bizbase.entity.PhotoInfo
import com.kuyuzhiqi.utils.common.FileUtils
import com.kuyuzhiqi.utils.common.MD5Utils
import java.io.File
import java.util.concurrent.ExecutionException

/**
 * 封装图片加载方法
 */

object LoadImageUtils {

    private const val IMAGE_ROUNDING_RADIUS = 12

    @JvmOverloads
    fun loadEmptyImage(context: Context, imageView: ImageView, isRoundedCorner: Boolean = false) {
        loadFromResource(context, R.drawable.bg_image_place_holder, imageView, isRoundedCorner)
    }

    @JvmOverloads
    fun loadFromDisk(context: Context, filePath: String, imageView: ImageView, isRoundedCorner: Boolean = false) {
        val requestBuilder = Glide.with(context).load(BizConstant.LOCAL_FILE_SCHEMA + filePath.trim { it <= ' ' })
        loadImage(requestBuilder, imageView, isRoundedCorner)
    }

    @JvmOverloads
    fun loadFromNetwork(context: Context, url: String, imageView: ImageView, isRoundedCorner: Boolean = false) {
        val requestBuilder = Glide.with(context).load(Uri.parse(url))
        loadImage(requestBuilder, imageView, isRoundedCorner)
    }

    @JvmOverloads
    fun loadFromResource(
            context: Context, @DrawableRes resourceId: Int,
            imageView: ImageView,
            isRoundedCorner: Boolean = false
    ) {
        val requestBuilder = Glide.with(context).load(resourceId)
        loadImage(requestBuilder, imageView, isRoundedCorner)
    }

    private fun loadImage(
            requestBuilder: RequestBuilder<Drawable>,
            imageView: ImageView,
            isRoundedCorner: Boolean
    ) {
        requestBuilder
                .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .apply(placeholderOf(R.drawable.bg_image_place_holder))
                .apply(errorOf(R.drawable.bg_image_place_holder))
        if (isRoundedCorner) {
            requestBuilder.apply(bitmapTransform(RoundedCorners(IMAGE_ROUNDING_RADIUS)))
        }
        requestBuilder.into(imageView)
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun loadAsFile(context: Context, url: String): File {
        return Glide.with(context)
                .load(Uri.parse(url))
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get()
    }

    /**
     * 保存到指定文件夹并返回PhotoInfo
     */
    @Throws(ExecutionException::class, InterruptedException::class)
    fun loadAsFileAndMove(context: Context, url: String, dir: String): PhotoInfo {
        val file = loadAsFile(context, url)
        val md5 = MD5Utils.calculate(file)
        val path = dir + md5
        FileUtils.copyFile(file.absolutePath, path)

        return PhotoInfo().apply {
            this.md5 = md5
            this.path = path
            this.url = url
        }
    }

    @JvmStatic
    fun pauseRequests(context: Context) {
        if (Util.isOnMainThread()) {
            Glide.with(context).pauseRequests()
        }
    }

    fun loadAsBitmapFromUrl(context: Context, imageUrl: String, target: SimpleTarget<Bitmap>) {
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .apply(diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .apply(placeholderOf(R.drawable.bg_image_place_holder))
                .apply(errorOf(R.drawable.bg_image_place_holder))
                .into(target)
    }

    /**
     * 创建RecyclerView的暂停加载滚动监听器
     */
    fun createAutoPauseLoadRequestsScrollListenerForRV(): androidx.recyclerview.widget.RecyclerView.OnScrollListener {
        return AutoPauseLoadRequestsRecyclerViewScrollListener()
    }

    /**
     * 当快速滚动时暂停加载(RecyclerView)
     */
    private class AutoPauseLoadRequestsRecyclerViewScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            recyclerView.let {
                val context = it.context
                if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
                    if (Glide.with(context).isPaused) {
                        Glide.with(context).resumeRequests()
                    }
                } else {
                    if (!Glide.with(context).isPaused) {
                        Glide.with(context).pauseRequests()
                    }
                }
            }
        }
    }
}

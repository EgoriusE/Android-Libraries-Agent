package ${packageName}

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

/**
 * Example using Glide for load images into ImageView
 */
fun ImageView.loadImage(url: String, thumbnailUrl: String) {
    Glide
        .with(this)
        .load(url)
        .centerCrop() // or other transformation: fitCenter(), circleCrop(), etc
        .thumbnail(Glide.with(this).load(thumbnailUrl))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(android.R.drawable.arrow_up_float)
        .transform(RoundedCorners(8))
        .error(android.R.drawable.ic_delete)
        .into(this)
}
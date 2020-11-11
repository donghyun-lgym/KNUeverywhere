package com.dongcompany.knueverywhere.ui.MapInfo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dongcompany.knueverywhere.R
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage

class Around_rvAdapter(val context: MapInfoActivity, var course: String, var CourseNum: Int) :
        RecyclerView.Adapter<Around_rvAdapter.ViewHolder>() {


    private var items: ArrayList<Info_coalition> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.around_recyclerview_item, parent, false)
        return ViewHolder(view, course, CourseNum, context)
    }

    override fun getItemCount(): Int = items.size
    fun getCollectionOfAdapter(): ArrayList<Info_coalition> = this.items

    override fun onBindViewHolder(holder: Around_rvAdapter.ViewHolder, position: Int) = holder.onBind(items.get(position));

    fun clear() = this.items.clear()

    fun addItem(c: Info_coalition): Around_rvAdapter {
        items.add(c)
        notifyDataSetChanged()
        return this
    }

    class ViewHolder(v: View, val course: String, val courseNum: Int, val context: Context) : RecyclerView.ViewHolder(v) {
        lateinit var nameText: TextView
        lateinit var typeText: TextView
        lateinit var timeText: TextView
        lateinit var addressText: TextView
        lateinit var contentText: TextView
        lateinit var imageView: ImageView
        lateinit var item:ConstraintLayout
        var mStorage = FirebaseStorage.getInstance("gs://knu-everywhere.appspot.com")


        init {
            nameText = v.findViewById(R.id.Around_item_nameTextView)
            typeText = v.findViewById(R.id.Around_item_typeTextView)
            timeText = v.findViewById(R.id.Around_item_timeTextView)
            addressText = v.findViewById(R.id.Around_item_addressTextView)
            contentText = v.findViewById(R.id.Around_item_contentTextView)
            imageView = v.findViewById(R.id.Around_item_ImageView)
            item = v.findViewById(R.id.Around_item)
        }

        fun onBind(c: Info_coalition) {
            val ref = mStorage.getReference().child("/" + course + "/" + courseNum + "/around/" + c.name + ".jpg")
            nameText.setText(c.name)
            typeText.setText(c.type)
            timeText.setText("영업시간 : " + c.time)
            addressText.setText("주소 : " + c.address)
            contentText.setText(c.content)

            Glide.with(context)
                    .using(FirebaseImageLoader())
                    .load(ref)
                    .into(imageView)

            item.setOnClickListener(View.OnClickListener {
                val intent = Intent()
                intent.setAction(Intent.ACTION_VIEW)
                intent.setData(Uri.parse("geo:0,0?q=" + c.address))
                context.startActivity(intent)
            })
        }
    }
}


class Info_coalition(var name: String, var time: String, var content: String, var type: String, var address: String)
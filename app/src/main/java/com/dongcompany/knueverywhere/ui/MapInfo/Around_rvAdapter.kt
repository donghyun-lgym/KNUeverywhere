package com.dongcompany.knueverywhere.ui.MapInfo

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dongcompany.knueverywhere.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class Around_rvAdapter(val context: MapInfoActivity) :
        RecyclerView.Adapter<Around_rvAdapter.ViewHolder>() {
    private var items: ArrayList<Info_coalition> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Around_rvAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.around_recyclerview_item, parent, false)
        return Around_rvAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size
    fun getCollectionOfAdapter(): ArrayList<Info_coalition> = this.items


    override fun onBindViewHolder(holder: Around_rvAdapter.ViewHolder, position: Int) {
        holder.onBind(items.get(position));
    }

    fun clear() = this.items.clear()


    fun addItem(c: Info_coalition): Around_rvAdapter {
        items.add(c)
        notifyDataSetChanged()
        return this
    }




    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        lateinit var nameText: TextView
        lateinit var typeText: TextView
        lateinit var timeText: TextView
        lateinit var addressText: TextView
        lateinit var contentText: TextView
        lateinit var imageView: ImageView

        init {
            nameText = v.findViewById(R.id.Around_item_nameTextView)
            typeText = v.findViewById(R.id.Around_item_typeTextView)
            timeText = v.findViewById(R.id.Around_item_timeTextView)
            addressText = v.findViewById(R.id.Around_item_addressTextView)
            contentText = v.findViewById(R.id.Around_item_contentTextView)
            imageView = v.findViewById(R.id.Around_item_ImageView)
        }

        fun onBind(c: Info_coalition) {
            nameText.setText(c.name)
            typeText.setText(c.type)
            timeText.setText("영업시간 : " + c.time)
            addressText.setText("주소 : " + c.address)
            contentText.setText(c.content)
        }
    }
}


class Info_coalition(var name: String, var time: String, var content: String, var type: String, var address : String)
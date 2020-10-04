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

class Review_rvAdapter(val context: MapInfoActivity) :
        RecyclerView.Adapter<Review_rvAdapter.ViewHolder>() {
    private var items: ArrayList<Info> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Review_rvAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_recyclerview_item, parent, false)
        return Review_rvAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size
    fun getCollectionOfAdapter(): ArrayList<Info> = this.items


    override fun onBindViewHolder(holder: Review_rvAdapter.ViewHolder, position: Int) {
        holder.onBind(items.get(position));
    }

    fun clear() = this.items.clear()


    fun addItem(c: Info): Review_rvAdapter {
        items.add(c)
        notifyDataSetChanged()
        return this
    }

    fun setListSort(): Unit
       = Collections.sort(items)



    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        lateinit var nameText: TextView
        lateinit var dateText: TextView
        lateinit var contentText: TextView

        init {
            nameText = v.findViewById(R.id.ReviewItem_nameTextView)
            dateText = v.findViewById(R.id.ReviewItem_dateTextView)
            contentText = v.findViewById(R.id.ReviewItem_contentTextView)
        }

        fun onBind(c: Info) {
            nameText.setText(c.name + " (" + c.id + ")")
            dateText.setText(c.date)
            contentText.setText(c.content)
        }
    }
}


class Info(var name: String, var id: String, var date: String, var content: String) : Comparable<Info> {
    override fun compareTo(other: Info): Int {
        return this.date.compareTo(other.date);
    }
}

package com.example.til

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.item_recycle.view.*
import java.util.ArrayList

class RecycleAdapter(private val datalist: ArrayList<Data>) : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecycleAdapter.ViewHolder, position: Int) {
        val data = datalist[position]
        val listener = View.OnClickListener { it ->
            val intent= Intent(holder.itemView?.context, DetailContent::class.java)
            intent.putExtra("Detail_ID", data.id)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
            //Toast.makeText(it.context, "Clicked ${data.title}", Toast.LENGTH_LONG).show()
        }
        holder.apply{
            bind(listener, data)
            itemView.tag=data
        }
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        private var view : View = v
        fun bind(listener: View.OnClickListener, data: Data){
            view.textTitle.text = data.title
            view.textContent.text = data.content
            view.setOnClickListener(listener)
        }
    }
}


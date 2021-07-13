package com.jasvir.dogsapp.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jasvir.dogsapp.R
import com.bumptech.glide.Glide
import com.jasvir.dogsapp.data.DogData
import kotlinx.android.synthetic.main.adapter_dogs.view.*

class DogAdapter(private var listOfdogs: List<DogData>, private val dogClickListener: OndogClickListener) :
    RecyclerView.Adapter<dogViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): dogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_dogs, parent, false)
        return dogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfdogs.size
    }

    override fun onBindViewHolder(holder: dogViewHolder, position: Int) {
        val dog = listOfdogs[position]
        holder.bindView(dog)
        holder.itemView.setOnClickListener {
            dogClickListener.dogClicked(dog)
        }
    }

    fun updatedogs(dogs: List<DogData>) {
        listOfdogs = dogs
        notifyDataSetChanged()
    }
}

class dogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(dog: DogData) {
        itemView.apply {

            Glide
                .with(this)
                .load(dog.url)
                .into(itemView.ivImage);

        }
    }
}

interface OndogClickListener {
    fun dogClicked(dog: DogData)
}

package com.jasvir.dogsapp.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jasvir.dogsapp.R
import com.bumptech.glide.Glide
import com.jasvir.dogsapp.data.DogData
import kotlinx.android.synthetic.main.adapter_dogs.view.*

/**
 * adapter for pagination
 *
 */
class DogAdapter() :
     PagingDataAdapter<DogData, DogViewHolder>(DogsDiffCallBack())  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_dogs, parent, false)
        return DogViewHolder(view)
    }


    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = getItem(position)!!
        holder.bindView(dog)
    }

}

/**
 * diff utils for pagination
 *
 */
class DogsDiffCallBack : DiffUtil.ItemCallback<DogData>() {
    override fun areItemsTheSame(oldItem: DogData, newItem: DogData): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: DogData, newItem: DogData): Boolean {
        return oldItem == newItem
    }
}


class DogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(dog: DogData) {
        itemView.apply {
            Glide
                .with(this)
                .load(dog.url)
                .into(itemView.ivImage);
        }
    }
}



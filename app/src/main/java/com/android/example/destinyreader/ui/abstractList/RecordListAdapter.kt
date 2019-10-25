package com.android.example.destinyreader.ui.abstractList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.example.destinyreader.R
import com.android.example.destinyreader.api.ASSETS_BASE_URL
import com.android.example.destinyreader.databinding.MainItemBinding
import com.android.example.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject
import com.android.example.destinyreader.jsonParser.jsonPresentationNode.JSONPresentationNode
import com.android.example.destinyreader.ui.abstractList.AbstractListFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class RecordListAdapter(clickListener: DestinyObjectListener) : AbstractListAdapter(
    clickListener
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordListAdapter.ViewHolder {

        return ViewHolder.from(parent)

    }

    class ViewHolder private constructor(binding: MainItemBinding) : AbstractListAdapter.ViewHolder(binding) {


        val icon: ImageView = binding.bookIcon
        val title: TextView = binding.bookTitleTextView

        override fun bind(item: JSONDestinyObject, clickListener: DestinyObjectListener) {
            val res = itemView.context.resources
            GlideApp.with(icon)
                .load(ASSETS_BASE_URL + item.displayProperties.icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(icon)
            title.text = item.displayProperties.name

            if (binding is MainItemBinding) {
                binding.clickListener = clickListener
                binding.jsonObject = item as? JSONDestinyObject
                binding.executePendingBindings()
            } else {
                throw Throwable("Invalid binding !")
            }

        }


        companion object {
            fun from(parent: ViewGroup): RecordListAdapter.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MainItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

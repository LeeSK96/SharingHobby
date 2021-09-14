package com.example.sharinghobby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharinghobby.databinding.ViewholderSearchResultItemBinding
import com.example.sharinghobby.model.result.SearchResultEntity

class SearchRecyclerAdapter: RecyclerView.Adapter<SearchRecyclerAdapter.SearchResultItemViewHolder>() {

    private var searchResultList: List<SearchResultEntity> = listOf()
    private lateinit var searchResultClickListener: (SearchResultEntity) -> Unit

    class SearchResultItemViewHolder(private val binding: ViewholderSearchResultItemBinding,
                                     val searchResultClickListener: (SearchResultEntity) -> Unit) : RecyclerView.ViewHolder(binding.root){


        fun bindData(data: SearchResultEntity) = with(binding){
            textTextView.text = data.name
            subtextTextView.text = data.fullAddress



           val b :ViewholderSearchResultItemBinding  =  binding.apply {
               textTextView.text = data.name
            }

           val st : String  =  with(binding) {
               "RETURN"
            }
        }

        fun bindViews(data: SearchResultEntity){
            binding.root.setOnClickListener{
                searchResultClickListener(data)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultItemViewHolder {
       //  LayoutInflater // view inflate
        val view = ViewholderSearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultItemViewHolder(view, searchResultClickListener)
    }

    override fun onBindViewHolder(holder: SearchResultItemViewHolder, position: Int) {
        holder.bindData(searchResultList[position])
        holder.bindViews(searchResultList[position])
    }

    override fun getItemCount(): Int = searchResultList.size

    fun setSearchResultList(searchResultList: List<SearchResultEntity>, searchResultClickListener: (SearchResultEntity) -> Unit){
        this.searchResultList = searchResultList
        this.searchResultClickListener = searchResultClickListener
        notifyDataSetChanged()
    }

}
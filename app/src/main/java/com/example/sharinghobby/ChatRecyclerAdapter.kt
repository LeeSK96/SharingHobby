package com.devingryu.firechatexample

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.sharinghobby.Account
import com.example.sharinghobby.DBConnector
import com.example.sharinghobby.R
import com.example.sharinghobby.databinding.RecyclerChatbubbleLeftBinding
import com.example.sharinghobby.databinding.RecyclerChatbubbleRightBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat

data class ChatMessage(val text: String="", val sent_at: Timestamp= Timestamp(0,0), val sent_by:String="")

class ChatRecyclerAdapter(val currentUID: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mContext : Context? = null
    val userNickname : HashMap<String,String> = hashMapOf()
    private fun getUserNick(uid: String){
        CoroutineScope(Dispatchers.Default).launch {
            val connector = DBConnector()
            val accountData= connector.getData<Account>(uid)
            if(accountData != null){
                userNickname[uid] = accountData.nickname
                runBlocking(Dispatchers.Main) {
                    notifyDataSetChanged() //RecyclerView에서 데이터가 변경되었으니 다시 확인해라
                }
            } else {
                userNickname.remove(uid)
            }
        }
    }
    var data : ArrayList<ChatMessage> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun addData(newData: ArrayList<ChatMessage>) {
        data.addAll(newData)
        data.sortByDescending { it.sent_at }
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        val holder = when(viewType){
            ViewType.LEFT.value -> LeftViewHolder(
                RecyclerChatbubbleLeftBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
            else /*ViewType.RIGHT.value*/ -> RightViewHolder(RecyclerChatbubbleRightBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            ViewType.LEFT.value -> {
                with(holder as LeftViewHolder){
                    binding.text.text = data[position].text
                    binding.text.background =
                        AppCompatResources.getDrawable(mContext!!,
                            if(position!=0 && data[position-1].sent_by == data[position].sent_by)
                                R.drawable.bubble_left
                            else R.drawable.bubble_left_tail
                        )
                    binding.user.visibility = if(data.size-1<=position || data[position+1].sent_by != data[position].sent_by) View.VISIBLE else View.GONE
                    binding.user.text = if (userNickname[data[position].sent_by] != null) {
                        userNickname[data[position].sent_by]
                    } else {
                        userNickname[data[position].sent_by] = "불러오는 중.."
                        getUserNick(data[position].sent_by)
                        "불러오는 중.."
                    }
                    binding.time.text = SimpleDateFormat("hh:mm").format(data[position].sent_at.toDate())
                }
            }
            else /*ViewType.RIGHT.value*/ -> {
                with(holder as RightViewHolder){
                    binding.text.text = data[position].text
                    binding.text.background =
                        AppCompatResources.getDrawable(mContext!!,
                            if(position!=0 && data[position-1].sent_by == data[position].sent_by)
                                R.drawable.bubble_right
                            else R.drawable.bubble_right_tail
                        )
                    binding.time.text = SimpleDateFormat("hh:mm").format(data[position].sent_at.toDate())
                }
            }
        }
        Firebase.firestore.collection("Users").get()
            .addOnSuccessListener {
                val docs = it.documents
                for( doc in docs){
                    // do something ...
                }
            }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if(data[position].sent_by == currentUID) ViewType.RIGHT.value
        else ViewType.LEFT.value
    }
    enum class ViewType(val value:Int) {
        LEFT(0),RIGHT(1)
    }

    inner class LeftViewHolder(val binding: RecyclerChatbubbleLeftBinding) : RecyclerView.ViewHolder(binding.root)
    inner class RightViewHolder(val binding: RecyclerChatbubbleRightBinding) : RecyclerView.ViewHolder(binding.root)
}


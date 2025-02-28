import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.PostViewModel
import org.w3c.dom.Text
import java.sql.Date
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class ConversationsAdapter(
    private val context: Context,
    private val sessionManager: SessionManager,
    private val onConversationClicked: (Conversation) -> Unit
) : RecyclerView.Adapter<ConversationsAdapter.ConversationsViewHolder>(){
    private val conversations = mutableListOf<Conversation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_conversations, parent, false)
        view.isClickable = true
        return ConversationsViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ConversationsViewHolder, position: Int) {
        holder.bind(conversations[position])
    }

    override fun getItemCount(): Int = conversations.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Conversation>) {
        conversations.clear()
        conversations.addAll(newList)
        notifyDataSetChanged()
    }

    fun formatTimeAgo(createdAt: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val createdDate = dateFormat.parse(createdAt)
            val now = java.util.Date()
            val diffInMillis = now.time - createdDate.time

            val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
            val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
            val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

            when {
                seconds < 60 -> "$seconds s"
                minutes < 60 -> "$minutes m"
                hours < 24 -> "$hours h"
                days < 7 -> "$days d"
                days < 30 -> "${days / 7} w"
                days < 365 -> "${days / 30} m"
                else -> "${days / 365} y"
            }
        } catch (e: Exception) {
            "unknown"
        }
    }

    inner class ConversationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val profileImageView : ImageView = itemView.findViewById(R.id.profileImageView)
        val userNameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val handleTextView: TextView = itemView.findViewById(R.id.handleTextView)
        val lastMessageTextView: TextView = itemView.findViewById(R.id.lastMessageTextView)
        val timeStampTextView : TextView = itemView.findViewById(R.id.timestampTextView)

        @SuppressLint("SetTextI18n")
        fun bind(conversation: Conversation){
            userNameTextView.text = conversation.name
            handleTextView.text = if(conversation.users.isNotEmpty()) "@${conversation.users[0].username}" else ""
            lastMessageTextView.text = if (conversation.messages.isNotEmpty()){
                conversation.messages.last().content
            }else{
                "Aucun message"
            }

            timeStampTextView.text = formatTimeAgo(conversation.createdAt)
            itemView.setOnClickListener{
                onConversationClicked(conversation)
            }

        }
    }

}
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.Conversation

class ConversationsAdapter(
    private val context: Context, private var conversations: List<Conversation>
) : RecyclerView.Adapter<ConversationsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val userNameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        var handleTextView: TextView = itemView.findViewById(R.id.handleTextView)
        var lastMessageTextView: TextView = itemView.findViewById(R.id.lastMessageTextView)
        var timeStampTextView: TextView = itemView.findViewById(R.id.timestampTextView)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_conversations, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversation = conversations[position]
        Glide.with(context).load("zbi").placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile).into(holder.profileImageView)

        holder.userNameTextView.text = conversation.name
        holder.handleTextView.text = "@${conversation.users.get(0).username}"
        holder.lastMessageTextView.text =
            conversation.messages.get(conversation.messages.size - 1).content
        holder.timeStampTextView.text = conversation.createdAt
    }

    override fun getItemCount(): Int {
        return conversations.size
    }

    fun updateData(newConversations: List<Conversation>) {
        conversations = newConversations
        notifyDataSetChanged()

    }

}
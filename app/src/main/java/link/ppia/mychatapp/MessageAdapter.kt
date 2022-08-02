package link.ppia.mychatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth


//it will extend from recyclerview.adapter
//we need 2 viewholders, 1 for sending and 1 for receiving
//<RecyclerView.ViewHolder> ← Here, we just implement the members in the adapter.Click on it.
//then, we need to pass 2 things. The context and the array of the messages.
class MessageAdapter(val context: Context, val messageList:ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //here we have to attach the layout according to the views that we have.
    //If it's a sent message, we attach the sent layout. If current is received message, we attach the received layout.
    //create 2 variables below to represent each message type to help us return the view. To return the view, we use method "getItem..." below above class SentViewHolder...
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //here, let's inflate the items

        if(viewType ==1){
            //inflate receive
            //now here, inflate the view. The rest are the concepts of a custom recyclerview.
            val view: View = LayoutInflater.from(context).inflate(R.layout.received, parent, false)
            //then, we just return this UserViewHolder, and passs in the view.
            return ReceiveViewHolder(view)
        }else{
            //inflate sent
            //now here, inflate the view. The rest are the concepts of a custom recyclerview.
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            //then, we just return this UserViewHolder, and passs in the view.
            return SentViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //here, we will create the message object which we will get from the messagelist, and get its position
        val currentMessage = messageList[position]

        //Since we have 2 viewholders sent and receive,
        //The below means, if the current holder is the SentViewHolder....
        if(holder.javaClass == SentViewHolder::class.java){
            //do the stuff for sent view holder

            val viewHolder = holder as SentViewHolder
            //set the message that is inside the message object.
            //currentMessage.message → belows extracts the message
            holder.sentMessage.text = currentMessage.message
            }else{
                //if the current holder is receive view holder
                val viewHolder = holder as ReceiveViewHolder
            //set the message that is inside the message object.
            //currentMessage.message → belows extracts the message
            holder.receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        //let's return the size of the messagelist
        return messageList.size
    }

    //This method will return an integer depending upon the view type.
    override fun getItemViewType(position: Int): Int {
        //get the current message
        val currentMessage = messageList[position]
        //if the uid of the user matches with the sender ID of the message, we have to inflate a viewholder
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
            //and in the else part, it means you are not logged in and other user are sending so you are receiving....
        } else{
            return ITEM_RECEIVE
            // according to the return types of the getItemViewType, we need to inflate the ITEM_SENT and receive above...so go up there..
        }
    }

    class SentViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView){
        //let's initialize here textview (bubble layout)
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)
    }

    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //initialize here the textview (bubble layout)
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)
    }


}
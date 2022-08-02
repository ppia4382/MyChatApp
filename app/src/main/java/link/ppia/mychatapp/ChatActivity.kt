package link.ppia.mychatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    //initialize all the ingredients in the layout
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbReference: DatabaseReference

    //to send the message to the database and to receive them, the below are necessary
    //this is used to create a unique room for the sender and receiver so that the message is private and is
    //not seen to all the users.
    var receiverRoom: String? = null
    var senderRoom: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //let's receive the intents from the UserAdapter
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        //these senderRoom and receiverRooms will create the unique room for privacy of messages for each user..
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbReference = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)


        //we need to set the layout manager to the recyclerview and set this recyclerview to the
        //to the recyclerview
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //logic for adding data to recyclerview
        //get data from the nodes in the database
        mDbReference.child("chats") .child(senderRoom!!).child("messages")
            .addValueEventListener(object:ValueEventListener{
                //here we need the members and use this snapshot to get the values from the
                //database and show them here in this recyclerview

                override fun onDataChange(snapshot: DataSnapshot) {
                    //we have to clear the arraylist because this loops
                    //we'll use a loop to show the message in this snapshot
                    messageList.clear()
                    for (postSnapshot in snapshot.children){

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        //when the sent button is clicked....
        //adding the message to the database...
        sendButton.setOnClickListener{
            //Send button sends the message to the database and message will be received by each user.

            //get what is written in the messagebox
            val message = messageBox.text.toString()
            //we need to create a message object using this message (which asks for message, and sender ID)
            val messageObject = Message(message, senderUid)
            //now we need to create nodes of chat by creating child for each
            //push will create a unique node whenever this push is called
            //then we need a value which is the message object...
            mDbReference.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    //since we updated the sender room, we also have to update the receiver room
                    mDbReference.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            //clear the messageBox after sending
            messageBox.setText(" ")

        }

    }
}
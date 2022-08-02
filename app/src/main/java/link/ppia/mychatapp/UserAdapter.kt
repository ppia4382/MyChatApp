package link.ppia.mychatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

//This is for the recyclerview (MainActivity)
//we need a context in this adapter because we'll need a context for the class because we'll use it in this class
//we'll then need a list of user (val userList of type ArrayList..of <User>
class UserAdapter(val context: Context, val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        //now here, inflate the view. The rest are the concepts of a custom recyclerview.
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        //then, we just return this UserViewHolder, and passs in the view.
        return UserViewHolder(view)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        //here we need to bind the things we are getting with the text that we just created
        //first, get the current user from the userlist
        val currentUser = userList[position]
        //then set the textview according to this user
        holder.textName.text = currentUser.name

        //when you want to send a message to a certain user, you'll have to click on that user, so we need onclickListeners...
        //it is done here on the useradapter, and onBindViewHolder
        holder.itemView.setOnClickListener{
            //to open up the chat activity...we have to pass the context of the activity but since we are not in an activity but in a class,
            //but we do have a context passed at the beginning of this class "class UserAdapter(val context: ..."
            //so we can simply put "context" in the parenthesis next to intent..then we need to go to ChatActivity..then class.java..
            val intent = Intent(context,ChatActivity::class.java )

            //we need to send information from this activity to the chat activity. Info is the name of the user which was clicked because
            //it has to be shown in the toolbar..
            //pass the nme of the user
            intent.putExtra("name", currentUser.name)
            //pass the uid of the user
            intent.putExtra("uid", currentUser?.uid)
            //↑ the info above has to be received so let's go to the ChatActivity to receive...


            //because we are not in an activity, we write
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        //We just have to return the size of the user
        return userList.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById<TextView>(R.id.txt_name)
    }

}
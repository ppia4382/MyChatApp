package link.ppia.mychatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView

    //the recyclerview needs the arraylist of users, and the adapter that was created
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    //bec we wanna show the users in the database, we need a reference to that database
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        //we then intialize the reference here
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        //now we need users to be added here in this recyclerview which we will get from the
        //database in firebase server.
        //The users cannot be taken from the Authentication. They have to be taken from the database.

        /**Now, we want to show the list of users in the recyclerView
         * Now that we have the userlist and adapter, now let's initialize the recyclerview.
         * */
        userRecyclerView = findViewById(R.id.userRecyclerView)
        /**When we use th recyclerview, we also need a layoutmanager
         * */
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        // set the adapter
        userRecyclerView.adapter = adapter
        //read the data stored in the database and show in the recyclerview
        //you'll have to go into all the user nodes in the db and read all the values.
        //The nodes can't be read by uids and so they have to be checked all one by one
        //and for this, the valueEventListeners will be added.
        mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //snapshot will be used to get data from database by getting a snapshot on how the schema of a particular database is.
                //First, all of the child in the snapshot has to be defined through the for loop below.
                //since this method will be called everytime, the first set of called data has to be removed by clearing
                userList.clear()
                for(postSnapshot in snapshot.children){
                    //since we have several users, we have to add the users and for this, we have to create user objects and add it in the user list.
                    //the .getValue gets the value of the user
                    val currentUser = postSnapshot.getValue(User::class.java)
                    //in the userlist of the user, the user doesn't have to see his/her own name for he/she will not
                    //exchange message with oneself.Because of this, the current user doesn't need to be displayed on the screen.
                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }

// You only add this if you want to show the user also in the list.*******************
//                    //then add the user to the user list and add the current user, and make it null safe
//                    userList.add(currentUser!!)
//*********************
                }
                    //the adapter also has to be notified about the data change
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.logout){
            //write the logic for logout
            mAuth.signOut()
            val intent = Intent(this@MainActivity, Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }

}
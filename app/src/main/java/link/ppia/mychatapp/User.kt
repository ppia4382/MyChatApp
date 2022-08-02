package link.ppia.mychatapp
// A model of the users has to be created. This represents the users in the server.

class User {

    //This user will have 3 properties the name, email , password that firebase provides for each users.
    //It can be null and will be set as null.
    var name: String? = null
    var email: String? = null
    var uid: String? = null

    constructor(){    }

    //each nullable
    constructor (name: String?,email: String?, uid:String?){
        //initialize
        this.name = name
        this.email = email
        this.uid = uid

        //so now, we need a recyclerview to show all of these. The layout for displaying that would be user_layout.xml
    }
}
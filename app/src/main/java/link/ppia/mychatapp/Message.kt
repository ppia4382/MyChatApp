package link.ppia.mychatapp

//This is the model of the message
class Message {
    //we basically have 2 things.Message and sender id.
    var message: String? = null
    var senderId: String? = null

    //we need a constructor
    constructor(){}

    //we pass the variables to the constructor
    constructor(message: String?, senderId: String?){
        this.message = message
        this.senderId = senderId
    }
}
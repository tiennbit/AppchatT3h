package t3hchat.thaivh.com.t3hchat.model;



public class Group extends Room{
    public String id;
    public ListFriend listFriend;

    public Group(){
        listFriend = new ListFriend();
    }
}

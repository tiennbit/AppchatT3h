package t3hchat.thaivh.com.t3hchat.model;

/**
 * Created by thais on 2/11/2018.
 */

public class ContactDto {

    String name;
    String tel;
    String img;

    public ContactDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

package sendyou.biz.senduandroid.data;

/**
 * Created by JunHyeok on 2016. 9. 26..
 */

public class Orders {
    private String address;
    private String image;
    private String numAddress;
    private String orderDate;
    private String orderStatus;
    private String orderUUID;
    private String receiverName;
    private String receiverPhone;
    private String text;
    private String userUUID;

    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return this.image;
    }
    public void setNumAddress(String numAddress){
        this.numAddress = numAddress;
    }
    public String getNumAddress(){
        return this.numAddress;
    }
    public void setOrderDate(String orderDate){
        this.orderDate = orderDate;
    }
    public String getOrderDate(){
        return this.orderDate;
    }
    public void setOrderStatus(String orderStatus){
        this.orderStatus = orderStatus;
    }
    public String getOrderStatus(){
        return this.orderStatus;
    }
    public void setOrderUUID(String orderUUID){
        this.orderUUID = orderUUID;
    }
    public String getOrderUUID(){
        return this.orderUUID;
    }
    public void setReceiverName(String receiverName){
        this.receiverName = receiverName;
    }
    public String getReceiverName(){
        return this.receiverName;
    }
    public void setReceiverPhone(String receiverPhone){
        this.receiverPhone = receiverPhone;
    }
    public String getReceiverPhone(){
        return this.receiverPhone;
    }
    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }
    public void setUserUUID(String userUUID){
        this.userUUID = userUUID;
    }
    public String getUserUUID(){
        return this.userUUID;
    }
}

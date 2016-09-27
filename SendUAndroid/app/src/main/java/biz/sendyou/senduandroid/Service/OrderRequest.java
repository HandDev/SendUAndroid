package biz.sendyou.senduandroid.Service;

/**
 * Created by JunHyeok on 2016. 9. 25..
 */

public class OrderRequest {
    final String userUUID,orderUUID,orderDate,receiverName,address,numAddress,text,image;

    OrderRequest(String userUUID,String orderUUID, String orderDate, String receiverName, String address,String numAddress,String text, String image) {
        this.userUUID = userUUID;
        this.orderUUID = orderUUID;
        this.orderDate = orderDate;
        this.receiverName = receiverName;
        this.address = address;
        this.numAddress = numAddress;
        this.text = text;
        this.image = image;
    }

}

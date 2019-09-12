package theatre.Seat;


import javafx.scene.image.ImageView;

public class Seat {
    private String type;
    private String seatImgPath;
    private String seatPosition;
    private int price;
    private boolean reserve;

    private ImageView seatImg;

    public Seat(String type, String seatImgPath, int price, boolean reserve) {
        this.type = type;
        this.seatImgPath = seatImgPath;
        this.price = price;
        this.reserve = reserve;
    }

    public String getType() {
        return type;
    }

    public String getSeatImgPath() {
        return seatImgPath;
    }

    public int getPrice() {
        return price;
    }

    public boolean isReserve() {
        return reserve;
    }

    public void setReserve(boolean reserve) {
        this.reserve = reserve;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSeatImg(ImageView seatImg) {
        this.seatImg = seatImg;
    }

    public void setSeatPosition(String seatPosition) {
        this.seatPosition = seatPosition;
    }

    public String getSeatPosition() {
        return this.seatPosition;
    }

    public ImageView getSeatImg() {
        return seatImg;
    }
}

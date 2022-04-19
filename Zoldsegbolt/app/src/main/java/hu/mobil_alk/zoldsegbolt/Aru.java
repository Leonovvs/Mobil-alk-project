package hu.mobil_alk.zoldsegbolt;

public class Aru {
    private String id;
    private String name;
    private String price;
    private String info;
    private final int imageResource;

    public Aru(String name, String price, String info, int imageResource) {
        this.name = name;
        this.price = price;
        this.info = info;
        this.imageResource = imageResource;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getInfo() {
        return info;
    }

    public int getImageResource() {
        return imageResource;
    }
}

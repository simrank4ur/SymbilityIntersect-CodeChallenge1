package iameskay.com.cryptocharts;

public class Currency {
    private String mName;
    private Double mPrice;
    private boolean mFavourite;

    Currency(String n, Double p, boolean f) {
        mName = n;
        mPrice = p;
        mFavourite = f;
    }

    Currency setFavourite(boolean b) {
        mFavourite = b;
        return this;
    }

    public String getName() {
        return mName;
    }

    public Double getPrice() {
        return mPrice;
    }

    public boolean getFavourite() { return mFavourite; }

}

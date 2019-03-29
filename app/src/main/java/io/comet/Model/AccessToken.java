package io.comet.Model;

public class AccessToken {
    public String aToken = "";
    public String rToken = "";

    public AccessToken(String tokenA, String tokenR) {
        aToken = tokenA;
        rToken = tokenR;
    }

    public boolean isEmpty() {
        return aToken.isEmpty();
    }
}

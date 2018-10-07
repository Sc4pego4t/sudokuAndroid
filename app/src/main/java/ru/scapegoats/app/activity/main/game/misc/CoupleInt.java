package ru.scapegoats.app.activity.main.game.misc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CoupleInt {
    private int v1;
    private int v2;

    public CoupleInt(int v1, int v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public int getV1() {
        return v1;
    }

    public int getV2() {
        return v2;
    }

    public void setV1(int v1) {
        this.v1 = v1;
    }

    public void setV2(int v2) {
        this.v2 = v2;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof CoupleInt)){
            throw new ClassCastException();
        }
        CoupleInt o=(CoupleInt)obj;
        return (v1==o.v1 && v2==o.v2);
    }

    @NonNull
    @Override
    public String toString() {
        return v1+""+v2;
    }
}

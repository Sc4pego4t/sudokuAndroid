package ru.scapegoats.app.activity.main.game;

import androidx.annotation.NonNull;

class CoupleInt {
    private int v1;
    private int v2;

    CoupleInt(int v1, int v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    int getV1() {
        return v1;
    }

    int getV2() {
        return v2;
    }

    @NonNull
    @Override
    public String toString() {
        return v1+""+v2;
    }
}

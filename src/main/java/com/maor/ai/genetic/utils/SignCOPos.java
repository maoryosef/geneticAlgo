package com.maor.ai.genetic.utils;

public enum SignCOPos {
    Beginning,
    End;

    public static SignCOPos IntToPos(int value) {
        SignCOPos pos;

        switch (value) {
            case 0:
                pos = SignCOPos.Beginning;
                break;
            case 1:
                pos = SignCOPos.End;
                break;
            default:
                pos = SignCOPos.End;
        }

        return pos;
    }
}

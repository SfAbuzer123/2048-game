package com.javarush.task.task35.task3513;

import java.awt.*;
import java.util.Objects;

public class Tile {
    int value;

    public Tile(int value) {
        this.value = value;
    }

    public Tile() {
        this.value = 0;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isEmpty(){
        return value == 0;
    }

    public Color getFontColor(){
        return value<16?new Color(0x776e65): new Color(0xf9f6f2);
    }

    public Color getTileColor(){
        if (value==0)
            return new Color(0xcdc1b4);
        else if (value==2)
            return new Color(0xeee4da);
        else if (value==4)
            return new Color(0xede0c8);
        else if (value==8)
            return new Color(0xf2b179);
        else if (value==16)
            return new Color(0xf59563);
        else if (value==32)
            return new Color(0xf67c5f);
        else if (value==64)
            return new Color(0xf65e3b);
        else if (value==128)
            return new Color(0xedcf72);
        else if (value==256)
            return new Color(0xedcc61);
        else if (value==512)
            return new Color(0xedc850);
        else if (value==1024)
            return new Color(0xedc53f);
        else if (value==2048)
            return new Color(0xedc22e);
        return new Color(0xff0000);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return value == tile.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

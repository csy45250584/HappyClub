package com.haokuo.midtitlebar;

/**
 * Created by zjf on 2018-08-03.
 */

public class BarStyle {

    private int backgroundColor;
    private float titleSize;
    private int titleColor;
    private boolean hasBackArrow;
    private int navigationIconId;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public float getTitleSize() {
        return titleSize;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public boolean isHasBackArrow() {
        return hasBackArrow;
    }

    public int getNavigationIconId() {
        return this.navigationIconId;
    }

    public BarStyle(int backgroundColor, float titleSize, int titleColor, boolean hasBackArrow, int navigationIconId) {
        this.backgroundColor = backgroundColor;
        this.titleSize = titleSize;
        this.titleColor = titleColor;
        this.hasBackArrow = hasBackArrow;
        this.navigationIconId = navigationIconId;
    }

    public static class Builder {
        private int backgroundColor;
        private float titleSize;
        private int titleColor;
        private boolean hasBackArrow;
        private int navigationIconId;

        public Builder() {
            backgroundColor = 0xFFFFFFFF;
            titleSize = 18;
            titleColor = 0xFFFFFFED;
            hasBackArrow = false;
            navigationIconId = 0;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setTitleSize(float titleSize) {
            this.titleSize = titleSize;
            return this;
        }

        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder setHasBackArrow(boolean hasBackArrow) {
            this.hasBackArrow = hasBackArrow;
            return this;
        }

        public Builder setNavigationIconId(int navigationIconId) {
            this.navigationIconId = navigationIconId;
            return this;
        }

        public BarStyle build() {
            return new BarStyle(this.backgroundColor, this.titleSize, this.titleColor, this.hasBackArrow, this.navigationIconId);
        }
    }
}

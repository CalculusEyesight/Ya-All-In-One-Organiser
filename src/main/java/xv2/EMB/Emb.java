package xv2.EMB;

public class Emb {
    private int i08;
    private boolean isPortrait;

    public int getI08() {
        return i08;
    }

    public void setI08(int i08) {
        this.i08 = i08;
    }

    public boolean isPortrait() {
        return isPortrait;
    }

    public void setPortrait(boolean isPortrait) {
        this.isPortrait = isPortrait;
    }

    public static class Image {
        private String name;
        private byte[] image;

        Image(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public byte[] getImage() {
            return image;
        }

        public void setImage(byte[] image) {
            this.image = image;
        }

        @Override
        public String toString() {
            return getName();
        }
    } 
}
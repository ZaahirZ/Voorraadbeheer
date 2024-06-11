package org.voorraadbeheer.Templates;

public abstract class ImageController {
    protected final String IMAGE_DIR = "product_images";
    protected final String DEFAULT_IMAGE_PATH = "product_images/defaultImage.png";

    public abstract void loadImage(String imagePath);
}
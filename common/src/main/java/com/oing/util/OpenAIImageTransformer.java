package com.oing.util;

public interface OpenAIImageTransformer {
    String analyzeImageToText(byte[] imageBytes);

    byte[] generateImageFromText(String promptText);
}

package me.creeper.www.postpigeon.language;

import org.jetbrains.annotations.Nullable;

public enum LangFile {

    DK("dk", "Creeprr");

    private final String path;
    private final String author;

    LangFile(final String path, final String author) {
        this.path = path;
        this.author = author;
    }

    public String getPath() {
        return "lang_" + path + ".yml";
    }

    public String getAuthor() {
        return author;
    }

    @Nullable
    public static LangFile fromString(final String exPath) {

        for(final LangFile file : LangFile.values()) {
            if(file.getPath().equals(exPath)) {
                return file;
            }
        }

        return null;

    }

}

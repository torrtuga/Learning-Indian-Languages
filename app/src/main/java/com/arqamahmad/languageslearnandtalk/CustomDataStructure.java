package com.arqamahmad.languageslearnandtalk;

/**
 * Created by B on 8/28/2016.
 * Creating a class so that the word and its translation & audio can be stored in one DS
 */
public class CustomDataStructure {

    private String mDefaultTranslationId;
    private String mTransledId;

    public CustomDataStructure(String defaultTranslatedId,String translatedId){
        mDefaultTranslationId = defaultTranslatedId;
        mTransledId = translatedId;
    }

    public String getmDefaultTranslationId() {
        return mDefaultTranslationId;
    }

    public String getmTransledId() {
        return mTransledId;
    }

}

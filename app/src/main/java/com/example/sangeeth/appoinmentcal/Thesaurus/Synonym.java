package com.example.sangeeth.appoinmentcal.Thesaurus;

/**
 * Created by Sangeeth on 5/16/18.
 */

public class Synonym {

    private String category;
    private String synonyms;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    @Override
    public String toString() {
        return "Synonym [Category=" + category + ", Synonyms = " + synonyms + "]";
    }

}

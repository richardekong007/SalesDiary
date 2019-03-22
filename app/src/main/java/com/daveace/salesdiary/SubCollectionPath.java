package com.daveace.salesdiary;

public class SubCollectionPath {

    private String collection;
    private String doc;
    private String subCollection;
    private String subDocId;
    private Object subDoc;

    public SubCollectionPath(String collection, String doc, String subCollection, String subDocId, Object subDoc) {
        this.collection = collection;
        this.doc = doc;
        this.subCollection = subCollection;
        this.subDocId = subDocId;
        this.subDoc = subDoc;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getSubDocId() {
        return subDocId;
    }

    public void setSubDocId(String subDocId) {
        this.subDocId = subDocId;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getSubCollection() {
        return subCollection;
    }

    public void setSubCollection(String subCollection) {
        this.subCollection = subCollection;
    }

    public Object getSubDoc() {
        return subDoc;
    }

    public void setSubDoc(Object subDoc) {
        this.subDoc = subDoc;
    }
}

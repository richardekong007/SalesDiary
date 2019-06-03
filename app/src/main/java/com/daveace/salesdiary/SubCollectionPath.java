package com.daveace.salesdiary;

public class SubCollectionPath {

    private String collection;
    private String doc;
    private String subCollection;
    private String subDocId;
    private Object subDoc;

//    public SubCollectionPath(String collection, String doc, String subCollection, String subDocId, Object subDoc) {
//        this.collection = collection;
//        this.doc = doc;
//        this.subCollection = subCollection;
//        this.subDocId = subDocId;
//        this.subDoc = subDoc;
//    }

    private SubCollectionPath(final Builder builder) {
        this.collection = builder.collection;
        this.doc = builder.doc;
        this.subCollection = builder.subCollection;
        this.subDocId = builder.subDocId;
        this.subDoc = builder.subDoc;
    }

    public String getCollection() {
        return collection;
    }

    public String getSubDocId() {
        return subDocId;
    }

    public String getDoc() {
        return doc;
    }

    public Object getSubDoc() {
        return subDoc;
    }

    public String getSubCollection() {
        return subCollection;
    }


    public static class Builder {
        private String collection;
        private String doc;
        private String subCollection;
        private String subDocId;
        private Object subDoc;

        public Builder setCollection(String collection) {
            this.collection = collection;
            return this;
        }

        public Builder setSubDocId(String subDocId) {
            this.subDocId = subDocId;
            return this;
        }

        public Builder setDoc(String doc) {
            this.doc = doc;
            return this;
        }

        public Builder setSubCollection(String subCollection) {
            this.subCollection = subCollection;
            return this;
        }

        public Builder setSubDoc(Object subDoc) {
            this.subDoc = subDoc;
            return this;
        }

        public SubCollectionPath build() {
            SubCollectionPath collectionPath =
                    new SubCollectionPath(this);
            if (collectionPath.collection == null
                    || collectionPath.collection.length() < 1)
                throw new IllegalStateException("provide valid collection path");
            if (collectionPath.doc == null
                    || collectionPath.doc.length() < 1)
                throw new IllegalStateException("provide valid document name");
            if (collectionPath.subCollection == null
                    || collectionPath.subCollection.length() < 1)
                throw new IllegalStateException("provide valid sub collection name");
            if (collectionPath.subDoc == null)
                throw new IllegalStateException("provide valid sub document name");
            if (collectionPath.subDocId == null
                    || collectionPath.subDocId.length() < 1)
                throw new IllegalStateException("provide valid sub document id");
            return collectionPath;
        }
    }
}

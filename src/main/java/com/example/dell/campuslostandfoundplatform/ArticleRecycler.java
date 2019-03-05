package com.example.dell.campuslostandfoundplatform;

public class ArticleRecycler {
    private String articleTypeName;
    private int background;
    public String getArticleTypeName() {
        return articleTypeName;
    }

    public void setArticleTypeName(String articleTypeName) {
        this.articleTypeName = articleTypeName;
    }

    public ArticleRecycler(String articleTypeName,int background){
        this.articleTypeName=articleTypeName;
        this.background=background;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}

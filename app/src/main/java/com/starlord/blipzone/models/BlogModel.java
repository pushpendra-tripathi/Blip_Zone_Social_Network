package com.starlord.blipzone.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BlogModel {
    int id;
    List<CommentModel> commentList;
    List<LikeModel> likeList;
    String createdAt;
    String lastUpdatedOn;
    String content;
    String imageUrl;
    int viewType;
    int user;
}

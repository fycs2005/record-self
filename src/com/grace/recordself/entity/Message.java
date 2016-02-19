package com.grace.recordself.entity;

/**
 * Created by fengyi on 16/2/6.
 */
public class Message extends BaseEntity {
    public String serverId;
    public long bodyId;
    public Body body;
    public String summary;
    public int read;
    public int favorite;
    public int locked;
    public int picTag;
    public String order;
    public long date;
}

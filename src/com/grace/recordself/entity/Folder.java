package com.grace.recordself.entity;

/**
 * Created by fengyi on 16/2/6.
 */
public class Folder extends BaseEntity {
    public long parentId;
    public String serverId;
    public String displayName;
    public String path;
    public int totalCount;
    public int unreadCount;
    public int abilities;
}

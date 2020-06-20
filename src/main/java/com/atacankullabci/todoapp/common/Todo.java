package com.atacankullabci.todoapp.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Todo {

    @Id
    private String id;
    private String todoName;
    private List<Item> itemList;
    private User user;

    public Todo() {
    }

    public Todo(String id, String todoName, List<Item> itemList, User user) {
        this.id = id;
        this.todoName = todoName;
        this.itemList = itemList;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTodoName() {
        return todoName;
    }

    public void setTodoName(String todoName) {
        this.todoName = todoName;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id='" + id + '\'' +
                ", todoName='" + todoName + '\'' +
                ", itemList=" + itemList +
                ", user=" + user +
                '}';
    }
}

package com.atacankullabci.todoapp.common;


import com.atacankullabci.todoapp.common.enums.EnumItemDependency;
import com.atacankullabci.todoapp.common.enums.EnumItemStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Item {

    @Id
    private String id;
    private String todoRefId;
    private String name;
    private String description;
    private Date deadline;
    private EnumItemStatus status;
    private EnumItemDependency dependency;

    public Item() {
    }

    public Item(String id, String todoRefId, String name, String description, Date deadline, EnumItemStatus status, EnumItemDependency dependency) {
        this.id = id;
        this.todoRefId = todoRefId;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.dependency = dependency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTodoRefId() {
        return todoRefId;
    }

    public void setTodoRefId(String todoRefId) {
        this.todoRefId = todoRefId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public EnumItemStatus getStatus() {
        return status;
    }

    public void setStatus(EnumItemStatus status) {
        this.status = status;
    }

    public EnumItemDependency getDependency() {
        return dependency;
    }

    public void setDependency(EnumItemDependency dependency) {
        this.dependency = dependency;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", todoRefId='" + todoRefId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", status=" + status +
                ", dependency=" + dependency +
                '}';
    }
}

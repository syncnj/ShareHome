/*
 * Copyright 2010-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package sharehome.com.androidsharehome2.model;

import java.util.*;

public class Task {
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("groupName")
    private String groupName = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("taskTitle")
    private String taskTitle = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("taskContent")
    private String taskContent = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("taskDuration")
    private Integer taskDuration = null;
    @com.google.gson.annotations.SerializedName("taskUsers")
    private List<String> taskUsers = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("taskSolved")
    private Boolean taskSolved = null;

    /**
     * An explanation about the purpose of this instance.
     *
     * @return groupName
     **/
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of groupName.
     *
     * @param groupName the new value
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * An explanation about the purpose of this instance.
     *
     * @return taskTitle
     **/
    public String getTaskTitle() {
        return taskTitle;
    }

    /**
     * Sets the value of taskTitle.
     *
     * @param taskTitle the new value
     */
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    /**
     * An explanation about the purpose of this instance.
     *
     * @return taskContent
     **/
    public String getTaskContent() {
        return taskContent;
    }

    /**
     * Sets the value of taskContent.
     *
     * @param taskContent the new value
     */
    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    /**
     * An explanation about the purpose of this instance.
     *
     * @return taskDuration
     **/
    public Integer getTaskDuration() {
        return taskDuration;
    }

    /**
     * Sets the value of taskDuration.
     *
     * @param taskDuration the new value
     */
    public void setTaskDuration(Integer taskDuration) {
        this.taskDuration = taskDuration;
    }

    /**
     * Gets taskUsers
     *
     * @return taskUsers
     **/
    public List<String> getTaskUsers() {
        return taskUsers;
    }

    /**
     * Sets the value of taskUsers.
     *
     * @param taskUsers the new value
     */
    public void setTaskUsers(List<String> taskUsers) {
        this.taskUsers = taskUsers;
    }

    /**
     * An explanation about the purpose of this instance.
     *
     * @return taskSolved
     **/
    public Boolean getTaskSolved() {
        return taskSolved;
    }

    /**
     * Sets the value of taskSolved.
     *
     * @param taskSolved the new value
     */
    public void setTaskSolved(Boolean taskSolved) {
        this.taskSolved = taskSolved;
    }

}

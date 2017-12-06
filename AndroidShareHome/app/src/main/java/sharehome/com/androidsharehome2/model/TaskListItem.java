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


public class TaskListItem {
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
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("taskUser")
    private String taskUser = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("taskSolved")
    private Boolean taskSolved = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("taskID")
    private Integer taskID = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("lastRotated")
    private String lastRotated = null;

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
     * An explanation about the purpose of this instance.
     *
     * @return taskUser
     **/
    public String getTaskUser() {
        return taskUser;
    }

    /**
     * Sets the value of taskUser.
     *
     * @param taskUser the new value
     */
    public void setTaskUser(String taskUser) {
        this.taskUser = taskUser;
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

    /**
     * An explanation about the purpose of this instance.
     *
     * @return taskID
     **/
    public Integer getTaskID() {
        return taskID;
    }

    /**
     * Sets the value of taskID.
     *
     * @param taskID the new value
     */
    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    /**
     * An explanation about the purpose of this instance.
     *
     * @return lastRotated
     **/
    public String getLastRotated() {
        return lastRotated;
    }

    /**
     * Sets the value of lastRotated.
     *
     * @param lastRotated the new value
     */
    public void setLastRotated(String lastRotated) {
        this.lastRotated = lastRotated;
    }

}

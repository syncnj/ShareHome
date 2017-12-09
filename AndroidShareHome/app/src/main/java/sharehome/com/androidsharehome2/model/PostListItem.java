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


public class PostListItem {
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("groupName")
    private String groupName = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("postTitle")
    private String postTitle = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("postContent")
    private String postContent = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("postUrgent")
    private Boolean postUrgent = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("postID")
    private Integer postID = null;

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
     * @return postTitle
     **/
    public String getPostTitle() {
        return postTitle;
    }

    /**
     * Sets the value of postTitle.
     *
     * @param postTitle the new value
     */
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    /**
     * An explanation about the purpose of this instance.
     *
     * @return postContent
     **/
    public String getPostContent() {
        return postContent;
    }

    /**
     * Sets the value of postContent.
     *
     * @param postContent the new value
     */
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    /**
     * An explanation about the purpose of this instance.
     *
     * @return postUrgent
     **/
    public Boolean getPostUrgent() {
        return postUrgent;
    }

    /**
     * Sets the value of postUrgent.
     *
     * @param postUrgent the new value
     */
    public void setPostUrgent(Boolean postUrgent) {
        this.postUrgent = postUrgent;
    }

    /**
     * An explanation about the purpose of this instance.
     *
     * @return postID
     **/
    public Integer getPostID() {
        return postID;
    }

    /**
     * Sets the value of postID.
     *
     * @param postID the new value
     */
    public void setPostID(Integer postID) {
        this.postID = postID;
    }

}

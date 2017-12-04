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


public class TaskItem {
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("operation")
    private String operation = null;
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("groupName")
    private String groupName = null;

    /**
     * An explanation about the purpose of this instance.
     *
     * @return operation
     **/
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the value of operation.
     *
     * @param operation the new value
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

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

}

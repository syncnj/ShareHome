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


public class ResultStringResponse {
    /**
     * An explanation about the purpose of this instance.
     */
    @com.google.gson.annotations.SerializedName("result")
    private String result = null;

    /**
     * An explanation about the purpose of this instance.
     *
     * @return result
     **/
    public String getResult() {
        return result;
    }

    /**
     * Sets the value of result.
     *
     * @param result the new value
     */
    public void setResult(String result) {
        this.result = result;
    }

}

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

package sharehome.com.androidsharehome2;

import java.util.*;

import sharehome.com.androidsharehome2.model.Task;
import sharehome.com.androidsharehome2.model.ResultStringResponse;
import sharehome.com.androidsharehome2.model.ListOfString;
import sharehome.com.androidsharehome2.model.PostList;
import sharehome.com.androidsharehome2.model.Post;
import sharehome.com.androidsharehome2.model.TaskList;


@com.amazonaws.mobileconnectors.apigateway.annotation.Service(endpoint = "https://5wyfvovh46.execute-api.us-east-1.amazonaws.com/Prod")
public interface AwscodestarsharehomelambdaClient {


    /**
     * A generic invoker to invoke any API Gateway endpoint.
     * @param request
     * @return ApiResponse
     */
    com.amazonaws.mobileconnectors.apigateway.ApiResponse execute(com.amazonaws.mobileconnectors.apigateway.ApiRequest request);
    
    /**
     * 
     * 
     * @param operation 
     * @param gn 
     * @return Task
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/", method = "GET")
    Task rootGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "operation", location = "query")
            String operation,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "gn", location = "query")
            String gn);
    
    /**
     * 
     * 
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/", method = "POST")
    void rootPost();
    
    /**
     * 
     * 
     * @param userName 
     * @param operation 
     * @return ListOfString
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/group", method = "GET")
    ListOfString groupGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "userName", location = "query")
            String userName,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "operation", location = "query")
            String operation);
    
    /**
     * 
     * 
     * @param userName 
     * @param groupName 
     * @param operation 
     * @return ResultStringResponse
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/group", method = "POST")
    ResultStringResponse groupPost(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "userName", location = "query")
            String userName,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "groupName", location = "query")
            String groupName,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "operation", location = "query")
            String operation);
    
    /**
     * 
     * 
     * @param groupName 
     * @return PostList
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/post", method = "GET")
    PostList postGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "groupName", location = "query")
            String groupName);
    
    /**
     * 
     * 
     * @param body 
     * @param operation 
     * @return ResultStringResponse
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/post", method = "POST")
    ResultStringResponse postPost(
            Post body,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "operation", location = "query")
            String operation);
    
    /**
     * 
     * 
     * @param userName 
     * @return ResultStringResponse
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/profile", method = "GET")
    ResultStringResponse profileGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "userName", location = "query")
            String userName);
    
    /**
     * 
     * 
     * @param userName 
     * @param body 
     * @return ResultStringResponse
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/profile", method = "POST")
    ResultStringResponse profilePost(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "userName", location = "query")
            String userName,
            ResultStringResponse body);
    
    /**
     * 
     * 
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/profile", method = "OPTIONS")
    void profileOptions();
    
    /**
     * 
     * 
     * @param groupName 
     * @return TaskList
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/task", method = "GET")
    TaskList taskGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "groupName", location = "query")
            String groupName);
    
    /**
     * 
     * 
     * @param body 
     * @param operation 
     * @return ResultStringResponse
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/task", method = "POST")
    ResultStringResponse taskPost(
            Task body,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "operation", location = "query")
            String operation);
    
}


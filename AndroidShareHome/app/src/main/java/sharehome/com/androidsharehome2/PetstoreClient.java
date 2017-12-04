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

import sharehome.com.androidsharehome2.model.Pets;


@com.amazonaws.mobileconnectors.apigateway.annotation.Service(endpoint = "https://ui77x4yb07.execute-api.us-east-1.amazonaws.com/test")
public interface PetstoreClient {


    /**
     * A generic invoker to invoke any API Gateway endpoint.
     * @param request
     * @return ApiResponse
     */
    com.amazonaws.mobileconnectors.apigateway.ApiResponse execute(com.amazonaws.mobileconnectors.apigateway.ApiRequest request);
    
    /**
     * 
     * 
     * @param type 
     * @param page 
     * @return Pets
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/pets", method = "GET")
    Pets petsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "type", location = "query")
            String type,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "page", location = "query")
            String page);
    
}


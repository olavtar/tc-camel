/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.ecosystemappeng.trustedcontent.routes;

import com.redhat.ecosystemappeng.trustedcontent.model.Tc;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Camel route definitions.
 */
@ApplicationScoped
public class Routes extends EndpointRouteBuilder {

    @Inject
    TCModules tcModules;

    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.json);
        rest("/tc")
            .get().to("direct:getTc")
            .post().type(Tc.class).to("direct:addTc");
        from(direct("getTc")).bean(tcModules, "listCVEs");
        from(direct("addTc")).bean(tcModules, "getContent");

        rest("/vex/{vexId}").get().to("direct:getVEX");
        from(direct("getVEX")).bean(tcModules, "getVEX");
    }

}

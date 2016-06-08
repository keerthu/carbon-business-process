/**
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 **/


package org.wso2.carbon.bpmn.rest.internal;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.bpmn.core.BPMNEngineService;
import org.wso2.carbon.bpmn.rest.BPMNRestServiceImpl;

/**
 * Rest component lookup for bpmnEngineService
 */
@Component(
        name = "org.wso2.carbon.bpmn.rest.BPMNRestService",
        service = BPMNRestServiceImpl.class,
        immediate = true)

public class RestServiceComponent {

    private static final Logger log = LoggerFactory.getLogger(RestServiceComponent.class);
    private BundleContext bundleContext;

    @Reference(
            name = "org.wso2.carbon.bpmn.core.BPMNEngineService",
            service = BPMNEngineService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unRegisterBPMNEngineService")
    public void setBpmnEngineService(BPMNEngineService engineService) {
        RestServiceContentHolder.getInstance().setBpmnEngineService(engineService);
    }

    protected void unRegisterBPMNEngineService(BPMNEngineService engineService) {
        RestServiceContentHolder.getInstance().setBpmnEngineService(null);
        if (log.isDebugEnabled()) {
            log.debug("Unregistered BPMNEngineService..");
        }
    }

    @Activate
    protected void activate(ComponentContext ctxt) {
        this.bundleContext = ctxt.getBundleContext();
        RestServiceContentHolder restServiceContentHolder = RestServiceContentHolder.getInstance();
        BPMNRestServiceImpl restService = new BPMNRestServiceImpl();
        restServiceContentHolder.setRestService(restService);
        log.info("Activated BPMN Rest Service Component Successfully.");
    }

    @Deactivate
    protected void deactivate(BundleContext bundleContext) {
        // Nothing to do
    }
}
